package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.dto.TerrariumDataSendDto;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.service.TerrariumDataService;
import com.example.smartTerrarium.service.TerrariumStateService;
import com.example.smartTerrarium.service.VentilationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/terrarium")
public class TerrariumDataController {
    @Autowired
    private final TerrariumDataService terrariumDataService;

    @PostMapping("/dataTerrarium")
    public List<TerrariumData> getTemperatureFromTerrarium(@RequestBody List<TerrariumDataDto> terrariumDataDto) {
            return (terrariumDataService.saveTerrariumData(terrariumDataDto));
    }
    @PostMapping("/sendData")
    public void sendSetting(@RequestBody TerrariumDataSendDto terrariumDataSendDto) {
        terrariumDataService.sendTerariumData(terrariumDataSendDto);
    }
}
