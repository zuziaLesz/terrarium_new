package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.dto.TerrariumDataSendDto;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.service.SettingService;
import com.example.smartTerrarium.service.TerrariumDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/sendData")
    public void sendSetting(@RequestBody TerrariumDataSendDto terrariumDataSendDto) {
        settingService.sendTerariumData(terrariumDataSendDto);
    }
}
