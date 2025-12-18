package com.example.smartTerrarium.service;

import com.example.smartTerrarium.entity.TerrariumState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@RequiredArgsConstructor
@Service
public class RaspberryService {
    @Autowired
    private final SettingService settingService;
    private final TerrariumStateService terrariumStateService;

    private boolean shouldTurnOnHeating(double temperature) {
        double settingTemperature = settingService.getCurrentSetting().getTemperature();
        return settingTemperature > temperature && !terrariumStateService.getCurrentTerrariumState().isHeating();
    }

    private boolean shouldTurnOffHeating(double temperature) {
        double settingTemperature = settingService.getCurrentSetting().getTemperature();
        return settingTemperature <= temperature && terrariumStateService.getCurrentTerrariumState().isHeating();
    }
}
