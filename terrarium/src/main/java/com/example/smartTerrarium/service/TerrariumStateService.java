package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.DashboardDto;
import com.example.smartTerrarium.dto.HistoryDto;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.exception.NoTerrariumStateException;
import com.example.smartTerrarium.repository.TerrariumDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TerrariumStateService {

    @Autowired
    private final TerrariumDataRepository terrariumDataRepository;
    private final SettingService settingService;

    public DashboardDto getCurrentTerrariumStateAndMapToDto() {
        TerrariumData terrariumData = getCurrentTerrariumState();
        return mapTerrariumDataToDto(terrariumData);
    }

    public TerrariumData getCurrentTerrariumState() {
        Integer userId = settingService.getCurrentSetting().getUserId();
        return terrariumDataRepository.findMostRecent(userId).orElseThrow(NoTerrariumStateException::new);
    }

    public HistoryDto buildHistory(int userId, String timeframe) {

    }

    private DashboardDto mapTerrariumDataToDto(TerrariumData terrariumData) {
        Setting currentSetting = settingService.getCurrentSetting();
        return DashboardDto.builder()
                .temperature(terrariumData.getTemperature())
                .humidity(terrariumData.getMoisture())
                .brightness(terrariumData.getBrightness())
                .name(currentSetting.getName())
                .description(currentSetting.getDescription())
                .lightStart(currentSetting.getLightStart())
                .lightEnd(currentSetting.getLightStop())
                .timeUntilWatering(checkTimeUntilWatering())
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

            // Same day but time already passed → next week
            if (candidate.isBefore(now)) {
                candidate = candidate.plusWeeks(1);
            }

            if (nextWatering == null || candidate.isBefore(nextWatering)) {
                nextWatering = candidate;
            }
        }

        // Convert to Timestamp (absolute moment)
        return Timestamp.valueOf(nextWatering);
    }
}
