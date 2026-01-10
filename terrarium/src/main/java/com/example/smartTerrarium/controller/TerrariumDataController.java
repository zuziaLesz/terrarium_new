package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.dto.TerrariumDataSendDto;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.service.SettingService;
import com.example.smartTerrarium.service.TerrariumDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/terrarium")
public class TerrariumDataController {
    @Autowired
    private final TerrariumDataService terrariumDataService;
    private final SettingService settingService;

    @PostMapping("/dataTerrarium")
    public List<TerrariumData> getTemperatureFromTerrarium(@RequestBody List<TerrariumDataDto> terrariumDataDto) {
            return (terrariumDataService.saveTerrariumData(terrariumDataDto));
    }
    @GetMapping("/sendData")
    public TerrariumDataSendDto sendSetting() {
        Setting dummySetting = new Setting();
        dummySetting.setId(1);
        dummySetting.setName("Test Plant");
        dummySetting.setDescription("Dummy setting for testing");
        dummySetting.setTemperature(22.5);
        dummySetting.setMoisture(45.0);
        dummySetting.setWaterOverWeek(200.0);
        dummySetting.setWateringDays("MONDAY, FRIDAY");
        dummySetting.setWateringMethod("Drip");
        dummySetting.setLightStart(LocalTime.of(6, 0));   // 6:00 AM
        dummySetting.setLightStop(LocalTime.of(18, 0));   // 6:00 PM
        dummySetting.setLightVolume(300.0);
        dummySetting.setCustom(true);
        dummySetting.setLastUpdated(new Date());
        dummySetting.setCurrentlyUsed(true);
        dummySetting.setUserId(66);
        return settingService.mapSettingToTerrariumDataSend(dummySetting);
    }
}
