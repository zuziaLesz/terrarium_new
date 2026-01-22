package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.CommendDto;
import com.example.smartTerrarium.dto.DashboardDto;
import com.example.smartTerrarium.dto.HistoryDto;
import com.example.smartTerrarium.dto.HistoryRangeDto;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.exception.NoTerrariumStateException;
import com.example.smartTerrarium.repository.TerrariumDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TerrariumStateService {

    @Autowired
    private final TerrariumDataRepository terrariumDataRepository;
    private final SettingService settingService;
    private final WebClient webClient;

    public DashboardDto getCurrentTerrariumStateAndMapToDto() {
        TerrariumData terrariumData = getCurrentTerrariumState();
        return mapTerrariumDataToDto(terrariumData);
    }

    public TerrariumData getCurrentTerrariumState() {
        Integer userId = settingService.getCurrentSetting().getUserId();
        return terrariumDataRepository.findMostRecent(userId).orElseThrow(NoTerrariumStateException::new);
    }

    public HistoryDto buildHistory(int plantId, String timeframe) {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setRange(new HistoryRangeDto());
        historyDto.setTimezone(ZoneId.systemDefault().toString());

        if (timeframe.equalsIgnoreCase("day")) {
            LocalDateTime now = LocalDateTime.now();
            historyDto.getRange().setStartTime(now);
            historyDto.getRange().setEndTime(now.minusHours(24));
            LocalDateTime currentHour = now;
            LocalDateTime currentTimeFrom = now.minusHours(24);

            // Build indexes (last 24 hours)
            List<String> indexes = new ArrayList<>();
            for (int i = 0; i <=23; i++) {
                LocalDateTime time = currentHour.minusHours(i);
                indexes.add(String.format("%02d:00", time.getHour()));
            }

            // Prepare series
            Map<String, List<Double>> series = new HashMap<>();
            series.put("temperature", new ArrayList<>());
            series.put("moisture", new ArrayList<>());
            series.put("brightness", new ArrayList<>());

            // Most recent per hour
            Map<Integer, TerrariumData> mostRecentPerHour = new HashMap<>();
            List<TerrariumData> terrariumDataList =
                    terrariumDataRepository.findAllByPlantIdAndTimestampAfter(plantId, currentTimeFrom)
                            .orElseThrow(NoTerrariumStateException::new);

            for (TerrariumData data : terrariumDataList) {
                int hour = data.getTimestamp().getHour();
                if (!mostRecentPerHour.containsKey(hour) ||
                        data.getTimestamp().isAfter(mostRecentPerHour.get(hour).getTimestamp())) {
                    mostRecentPerHour.put(hour, data);
                }
            }
            Double lastTemp = null;
            Double lastMoist = null;
            Double lastBright = null;

            // Fill series
            for (String label : indexes) {
                int hour = Integer.parseInt(label.split(":")[0]);
                TerrariumData data = mostRecentPerHour.get(hour);

                if (data != null) {
                    lastTemp = data.getTemperature();
                    lastMoist = data.getMoisture();
                    lastBright = data.getBrightness();
                }

                series.get("temperature").add(lastTemp);
                series.get("moisture").add(lastMoist);
                series.get("brightness").add(lastBright);
            }
            Collections.reverse(indexes);

            Collections.reverse(series.get("temperature"));
            Collections.reverse(series.get("moisture"));
            Collections.reverse(series.get("brightness"));
            historyDto.setIndexes(indexes);
            historyDto.setSeries(series);
        }
        else if(timeframe.equalsIgnoreCase("week")) {
            historyDto.getRange().setStartTime(LocalDateTime.now());
            historyDto.getRange().setEndTime(LocalDateTime.now().minusDays(7));
            LocalDateTime currentDay = historyDto.getRange().getStartTime();
            LocalDateTime currentTimeFrom = currentDay.minusDays(7);
            List<String> indexes = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                LocalDateTime time = currentDay.minusDays(i);
                indexes.add(time.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            }
            historyDto.setIndexes(indexes);
            Map<String, List<Double>> series = new HashMap<>();
            series.put("temperature", new ArrayList<>());
            series.put("moisture", new ArrayList<>());
            series.put("brightness", new ArrayList<>());
            Map<Integer, TerrariumData> mostRecentPerDay = new HashMap<>();
            List<TerrariumData> terrariumDataList = terrariumDataRepository.findAllByPlantIdAndTimestampAfter(plantId, currentTimeFrom).orElseThrow(NoTerrariumStateException::new);
            for (TerrariumData data : terrariumDataList) {
                int day = data.getTimestamp().getDayOfWeek().getValue();

                if (!mostRecentPerDay.containsKey(day) ||
                        data.getTimestamp().isAfter(mostRecentPerDay.get(day).getTimestamp())) {

                    mostRecentPerDay.put(day, data);
                }
            }
            Double lastTemp = null;
            Double lastMoist = null;
            Double lastBright = null;
            for (String label : indexes) {
                DayOfWeek dayOfWeek = DayOfWeek.valueOf(
                        switch (label) {
                            case "Mon" -> "MONDAY";
                            case "Tue" -> "TUESDAY";
                            case "Wed" -> "WEDNESDAY";
                            case "Thu" -> "THURSDAY";
                            case "Fri" -> "FRIDAY";
                            case "Sat" -> "SATURDAY";
                            case "Sun" -> "SUNDAY";
                            default -> throw new IllegalArgumentException("Invalid day label: " + label);
                        }
                );
                int dayValue = dayOfWeek.getValue();

                TerrariumData data = mostRecentPerDay.get(dayValue);

                if (data != null) {
                    lastTemp = data.getTemperature();
                    lastMoist = data.getMoisture();
                    lastBright = data.getBrightness();
                }
                series.get("temperature").add(lastTemp);
                series.get("moisture").add(lastMoist);
                series.get("brightness").add(lastBright);
            }
            historyDto.setSeries(series);
        }
        else if(timeframe.equalsIgnoreCase("month")) {
            historyDto.getRange().setStartTime(LocalDateTime.now());
            historyDto.getRange().setEndTime(LocalDateTime.now().minusDays(30));
            LocalDateTime currentDay = historyDto.getRange().getStartTime();
            LocalDateTime currentTimeFromMonth = currentDay.minusDays(30);

            List<String> dayIndexes = new ArrayList<>();
            for (int i = 0; i <= 30; i++) {
                LocalDateTime day = currentDay.minusDays(i);
                dayIndexes.add(day.getDayOfMonth() + "/" + day.getMonthValue());
            }
            Collections.reverse(dayIndexes);
            historyDto.setIndexes(dayIndexes);

            Map<Integer, TerrariumData> mostRecentPerDay = new HashMap<>();
            List<TerrariumData> terrariumDataMonth =
                    terrariumDataRepository.findAllByPlantIdAndTimestampAfter(plantId, currentTimeFromMonth)
                            .orElseThrow(NoTerrariumStateException::new);

            for (TerrariumData data : terrariumDataMonth) {
                int dayOfMonth = data.getTimestamp().getDayOfMonth();
                if (!mostRecentPerDay.containsKey(dayOfMonth) ||
                        data.getTimestamp().isAfter(mostRecentPerDay.get(dayOfMonth).getTimestamp())) {
                    mostRecentPerDay.put(dayOfMonth, data);
                }
            }
            Map<String, List<Double>> series = new HashMap<>();
            series.put("temperature", new ArrayList<>());
            series.put("moisture", new ArrayList<>());
            series.put("brightness", new ArrayList<>());

            Double lastTemp = null;
            Double lastMoist = null;
            Double lastBright = null;
            for (String label : dayIndexes) {
                int dayOfMonth = Integer.parseInt(label.split("/")[0]);
                TerrariumData data = mostRecentPerDay.get(dayOfMonth);

                if (data != null) {
                    lastTemp = data.getTemperature();
                    lastMoist = data.getMoisture();
                    lastBright = data.getBrightness();
                }
                series.get("temperature").add(lastTemp);
                series.get("moisture").add(lastMoist);
                series.get("brightness").add(lastBright);
            }
            historyDto.setSeries(series);
        }

        return historyDto;
    }

    public void waterPlant() {
        CommendDto dto = new CommendDto();
        Setting setting = settingService.getCurrentSetting();
        if(setting.getWateringMethod().equalsIgnoreCase("sprinkler")) {
            dto.setComponent("sprinkler");
        }
        else if(setting.getWateringMethod().equalsIgnoreCase("standard")) {
            dto.setComponent("pump");
        }
        dto.setState("on");
        webClient.post()
                .uri("https://api.leafcore.eu/external/watering")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void lightPlant(Double brightness) {
        Setting setting = settingService.getCurrentSetting();
        setting.setLightVolume(brightness);
        settingService.save(setting);
        CommendDto dto = new CommendDto();
        dto.setComponent("light");
        if(brightness == 0) {
            dto.setState("off");
        }
        else dto.setState("on");
        dto.setIntensity(brightness.toString());
        webClient.post()
                .uri("https://api.leafcore.eu/external/light")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private DashboardDto mapTerrariumDataToDto(TerrariumData terrariumData) {
        Setting currentSetting = settingService.getCurrentSetting();
        return DashboardDto.builder()
                .temperature(terrariumData.getTemperature())
                .humidity(terrariumData.getMoisture())
                .brightness(terrariumData.getBrightness())
                .water_level(terrariumData.getWaterLevel())
                .name(currentSetting.getName())
                .description(currentSetting.getDescription())
                .lightStart(currentSetting.getLightStart())
                .lightEnd(currentSetting.getLightStop())
                .timeUntilWatering(checkTimeUntilWatering())
                .image(currentSetting.getImage())
                .build();
    }

    private Timestamp checkTimeUntilWatering() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        Setting currentSetting = settingService.getCurrentSetting();
        List<DayOfWeek> wateringDays = settingService.mapWateringDaysToList(currentSetting.getWateringDays())
                .stream()
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf)
                .toList();
        int hour = 6;
        int minute = 0;

        LocalDateTime nextWatering = null;

        for (DayOfWeek day : wateringDays) {

            int diff = day.getValue() - today.getDayOfWeek().getValue();
            if (diff < 0) diff += 7;

            LocalDate targetDate = today.plusDays(diff);
            LocalDateTime candidate =
                    targetDate.atTime(hour, minute);

            if (candidate.isBefore(now)) {
                candidate = candidate.plusWeeks(1);
            }

            if (nextWatering == null || candidate.isBefore(nextWatering)) {
                nextWatering = candidate;
            }
        }

        return Timestamp.valueOf(nextWatering);
    }
}
