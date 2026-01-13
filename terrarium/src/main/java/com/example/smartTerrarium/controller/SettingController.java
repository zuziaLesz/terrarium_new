package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.dto.CreateSettingDto;
import com.example.smartTerrarium.dto.GetSettingDto;
import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class SettingController {
    private final SettingService settingService;
    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping(value = "/setting", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createSetting(@ModelAttribute CreateSettingDto createSettingDto) throws IOException {
        settingService.createSetting(createSettingDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/setting/{id}")
    public ResponseEntity<Void> editSetting(@PathVariable Integer id, @RequestBody CreateSettingDto createSettingDto) {
        settingService.editSetting(id, createSettingDto);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/image/{settingId}")
    public ResponseEntity<Void> editSetting(@PathVariable Integer settingId, @RequestParam("file") MultipartFile file) throws IOException {
        settingService.editImage(file, settingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/setting")
    public ResponseEntity<List<GetSettingDto>>getAllSettings() {
        return ResponseEntity.ok(settingService.getAllSettings());
    }

    @GetMapping("/setting/{id}")
    public ResponseEntity<GetSettingDto> getSettingById(@PathVariable Integer id) {
        return ResponseEntity.ok(settingService.getSetting(id));
    }

    @GetMapping("/setting/current")
    public ResponseEntity<GetSettingDto> getCurrentSetting() {
        return ResponseEntity.ok(settingService.getCurrentSettingAndMap());
    }

    @PostMapping("/applySetting/{id}")
    public ResponseEntity<TerrariumDataDto> applySetting(@PathVariable Integer id) {
        return ResponseEntity.ok(settingService.applySetting(id));
    }

    @DeleteMapping("/setting/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Integer id) {
        settingService.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }
}
