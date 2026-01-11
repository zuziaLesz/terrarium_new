package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.CreateModuleDto;
import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.dto.TerrariumDataSendDto;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.service.ModuleService;
import com.example.smartTerrarium.service.SettingService;
import com.example.smartTerrarium.service.TerrariumDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private final ModuleService moduleService;

    @PostMapping("/dataTerrarium/{groupId}")
    public ResponseEntity<List<TerrariumData>> getTemperatureFromTerrarium(@RequestBody List<TerrariumDataDto> terrariumDataDto, @PathVariable String groupId) {
            return ResponseEntity.ok(terrariumDataService.saveTerrariumData(terrariumDataDto, groupId));
    }
    @PostMapping("/module")
    public ResponseEntity<Void> addModule(@RequestBody CreateModuleDto createModuleDto) {
        moduleService.add(createModuleDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/sendData/{groupId}")
    public ResponseEntity<TerrariumDataSendDto> sendSetting(@PathVariable String groupId) {
        Setting setting = terrariumDataService.getCurrentSettingByGroupId(groupId);
        return ResponseEntity.ok(settingService.mapSettingToTerrariumDataSend(setting));
    }
    @PostMapping("/updateSetting/{groupId}")
    public ResponseEntity<Void> updateSetting(@PathVariable String groupId, @RequestBody TerrariumDataSendDto dto) {
        terrariumDataService.mapTerrariumSendDataToSettingAndSave(dto, groupId);
        return ResponseEntity.ok().build();
    }
}
