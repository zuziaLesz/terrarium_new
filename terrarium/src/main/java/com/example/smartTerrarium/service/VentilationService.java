package com.example.smartTerrarium.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class VentilationService {
    private final SettingService settingService;
    private final TerrariumStateService terrariumStateService;

    private boolean humidifier = false;

    private String checkIfTurnOnVentilation(double moisture) {
        Double settingMoisture = settingService.getCurrentSetting().getMoisture();
        if(moisture>settingMoisture && !getVentilationStatus()) {
            terrariumStateService.changeVentilation(true);
            return "on";
        }
        else if(moisture<=settingMoisture && getVentilationStatus()) {
            terrariumStateService.changeVentilation(false);
            return "off";
        }
        else return "null";
    }

    private String checkIfTurnOnHumidifier(double moisture) {
        Double settingMoisture = settingService.getCurrentSetting().getMoisture();
        if(moisture<settingMoisture && !humidifier) {
            humidifier = true;
            return "on";
        }
        else if(moisture>settingMoisture && humidifier) {
            humidifier = false;
            return "off";
        }
        else return "null";
    }

    private boolean getVentilationStatus() {
        return terrariumStateService.getCurrentTerrariumState().isVentilation();
    }
}
