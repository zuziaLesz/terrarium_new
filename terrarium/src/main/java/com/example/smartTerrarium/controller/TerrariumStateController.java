package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.DashboardDto;
import com.example.smartTerrarium.dto.TerrariumStateDto;
import com.example.smartTerrarium.service.TerrariumStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TerrariumStateController {
    @Autowired
    private final TerrariumStateService terrariumStateService;

    @GetMapping("/terrarium_state/current")
    public ResponseEntity<DashboardDto> getCurrentTerrariumState() {
        return ResponseEntity.ok(terrariumStateService.getCurrentTerrariumStateAndMapToDto());
    }
}
