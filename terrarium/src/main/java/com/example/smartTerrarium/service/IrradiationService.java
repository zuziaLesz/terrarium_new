package com.example.smartTerrarium.service;

import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class IrradiationService {
    @Autowired
    private final SettingRepository settingRepository;
    private final TerrariumStateService terrariumStateService;
    private final UserService userService;

    private Setting getCurrentSetting() {
        int currentUser = userService.getCurrentUser().getId();
        return settingRepository.findCurrentlyUsed(currentUser).orElseThrow(() -> new RuntimeException("No currently used setting"));
    }

    private LocalTime getStartIrradiationTime() {
        Setting setting = getCurrentSetting();
        return setting.getLightStart();
    }
    private LocalTime getEndIrradiationTime() {
        Setting setting = getCurrentSetting();
        return setting.getLightStop();
    }
}
