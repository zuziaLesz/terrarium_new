package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.RaspberrySetting;
import com.example.smartTerrarium.dto.RaspberryState;
import com.example.smartTerrarium.dto.SendTerrariumCommandDto;
import com.example.smartTerrarium.service.RaspberryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/raspberry")
public class RaspberryController {
    public final RaspberryService raspberryService;
    @Autowired
    public RaspberryController(RaspberryService raspberryService) {this.raspberryService = raspberryService;}
    @PostMapping("/state")
    public ResponseEntity<Void> getState(@RequestBody RaspberryState raspberryState) {
        raspberryService.saveState(raspberryState);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/heating")
    public SendTerrariumCommandDto sendHeating(@RequestBody RaspberrySetting raspberrySetting) {
        return raspberryService.sendHeating(raspberrySetting);
    }
}
