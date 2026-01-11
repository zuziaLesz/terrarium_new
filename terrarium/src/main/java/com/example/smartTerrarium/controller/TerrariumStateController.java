package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.DashboardDto;
import com.example.smartTerrarium.dto.HistoryDto;
import com.example.smartTerrarium.service.TerrariumStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TerrariumStateController {
    @Autowired
    private final TerrariumStateService terrariumStateService;


    @GetMapping("/terrarium_state/current")
    public ResponseEntity<DashboardDto> getCurrentTerrariumState() {
        return ResponseEntity.ok(terrariumStateService.getCurrentTerrariumStateAndMapToDto());
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<HistoryDto> getHistory(@PathVariable int id, @RequestParam String timeFrame) {
        return ResponseEntity.ok(terrariumStateService.buildHistory(id, timeFrame));
    }

    @PostMapping("/watering")
    public ResponseEntity<Void> waterPlant() {
        terrariumStateService.waterPlant();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/light")
    public ResponseEntity<Void> lightPlant(@RequestParam Double brightness) {
        terrariumStateService.lightPlant(brightness);
        return ResponseEntity.ok().build();
    }
}
