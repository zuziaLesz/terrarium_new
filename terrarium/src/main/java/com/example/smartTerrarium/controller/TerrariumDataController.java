package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.SendTerrariumCommandDto;
import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.service.TerrariumDataService;
import com.example.smartTerrarium.service.TerrariumStateService;
import com.example.smartTerrarium.service.VentilationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class TerrariumDataController {
    @Autowired
    private final TerrariumDataService terrariumDataService;
    private final TerrariumStateService terrariumStateService;
    private final VentilationService ventilationService;

    @GetMapping("/dataTerrarium")
    public ResponseEntity<Void> getTemperatureFromTerrarium(@RequestBody TerrariumDataDto terrariumDataDto) {
            TerrariumData terrariumData = terrariumDataService.saveTerrariumData(terrariumDataDto);
            terrariumStateService.addNewTerrariumState(terrariumData);
            return ResponseEntity.noContent().build();
    }
    @PostMapping("/ventilation")
    public ResponseEntity<SendTerrariumCommandDto> sendVentilationCommand() throws IOException {
        double moisture = terrariumStateService.getCurrentTerrariumStateAndMapToDto().getMoisture();
        return ResponseEntity.ok(ventilationService.sendVentilationCommand(moisture));
    }

    @PostMapping("/humidifier")
    public ResponseEntity<SendTerrariumCommandDto> sendHumidifierCommand() throws IOException {
        double moisture = terrariumStateService.getCurrentTerrariumStateAndMapToDto().getMoisture();
        return ResponseEntity.ok(ventilationService.sendHumidifierCommand(moisture));
    }

}
