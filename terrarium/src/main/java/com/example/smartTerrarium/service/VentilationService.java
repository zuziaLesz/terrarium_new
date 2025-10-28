package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.SendTerrariumCommandDto;
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

    private SendTerrariumCommandDto buildCommand(String message, int id) throws IOException {
        SendTerrariumCommandDto command = new SendTerrariumCommandDto();
        command.setId(id);
        command.setCommand(message);
        String[] cmd = { "bash", "-c", "home/ubuntu/PythonScripts/change_device_mode.py " + id + " " + message };
        Process p = Runtime.getRuntime().exec(cmd);
        return command;
    }

    public SendTerrariumCommandDto sendHumidifierCommand(double moisture) throws IOException {
        return buildCommand(checkIfTurnOnHumidifier(moisture), 17);
    }

    public SendTerrariumCommandDto sendVentilationCommand(double moisture) throws IOException {
        return buildCommand(checkIfTurnOnVentilation(moisture), 22);
    }

    private boolean getVentilationStatus() {
        return terrariumStateService.getCurrentTerrariumState().isVentilation();
    }
}
