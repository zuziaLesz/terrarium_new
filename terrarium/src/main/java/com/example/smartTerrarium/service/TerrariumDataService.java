package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.dto.TerrariumDataSendDto;
import com.example.smartTerrarium.entity.Module;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.repository.ModuleRepository;
import com.example.smartTerrarium.repository.TerrariumDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TerrariumDataService {

    private TerrariumDataRepository terrariumDataRepository;
    private final SettingService settingService;
    private final ModuleRepository moduleRepository;

    @Autowired
    public TerrariumDataService(TerrariumDataRepository terrariumDataRepository, SettingService settingService, ModuleRepository moduleRepository) {
        this.terrariumDataRepository = terrariumDataRepository;
        this.settingService = settingService;
        this.moduleRepository = moduleRepository;
    }
    public List<TerrariumData> saveTerrariumData (List<TerrariumDataDto> terrariumDataDto, Setting setting, String groupId) {
        List<TerrariumData> terrariumDataList = terrariumDataDto.stream()
                .map(dto -> TerrariumData.builder()
                        .temperature(dto.getTemperature())
                        .moisture(dto.getMoisture())
                        .brightness(dto.getBrightness())
                        .lastUpdate(dto.getTimestamp())
                        .userId(findUserByGroupId(groupId))
                        .plantId(setting.getId())
                        .build()
                )
                .toList();

        return terrariumDataRepository.saveAll(terrariumDataList);
    }
    public Setting getCurrentSettingByGroupId(String groupId) {
        Integer userId = findUserByGroupId(groupId);
        return settingService.getCurrentSettingByUserId(userId);
    }
    private Integer findUserByGroupId(String groupId) {
        Module module = moduleRepository.findFirstByGroupId(groupId).orElseThrow();
        return module.getUserId();
    }
    private List<String> mapWteringDaysToList(String days) {
        return Arrays.stream(days.split(","))
                .collect(Collectors.toList());
    }
    public void mapTerrariumSendDataToSettingAndSave(TerrariumDataSendDto dto, String groupId) {
        Setting setting = getCurrentSettingByGroupId(groupId);
        if (dto.getPlant_name() != null) {
            setting.setName(dto.getPlant_name());
        }
        if (dto.getOptimal_temperature() != null) {
            setting.setTemperature(Double.valueOf(dto.getOptimal_temperature()));
        }
        if(dto.getOptimal_humidity() != null) {
            setting.setMoisture(Double.valueOf(dto.getOptimal_humidity()));
        }
        if(dto.getOptimal_brightness() != null) {
            setting.setLightVolume(Double.valueOf(dto.getOptimal_brightness()));
        }
        if(dto.getLight_schedule_start_time() != null) {
            setting.setLightStart(LocalTime.parse(dto.getLight_schedule_start_time(), DateTimeFormatter.ofPattern("HH:mm")));
        }
        if(dto.getLight_schedule_end_time() != null) {
            setting.setLightStop(LocalTime.parse(dto.getLight_schedule_end_time(), DateTimeFormatter.ofPattern("HH:mm")));
        }
        if(dto.getWatering_mode() != null) {
            setting.setWateringMethod(dto.getWatering_mode());
        }
        if(dto.getWater_amount() != null) {
            setting.setWaterOverWeek(dto.getWater_amount().doubleValue());
        }
        if(dto.getLight_intensity() != null) {
            setting.setLightVolume(Double.valueOf(dto.getLight_intensity()));
        }
        if(dto.getDayOfWeek() != null && !dto.getDayOfWeek().isEmpty()) {
            setting.setWateringDays(settingService.mapWateringDaysToString(dto.getDayOfWeek()));
        }
        setting.setLastUpdated(new Date());
        settingService.save(setting);
    }
}
