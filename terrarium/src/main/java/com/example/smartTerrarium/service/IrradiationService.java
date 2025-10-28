package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.SendTerrariumCommandDto;
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

    private Setting getCurrentSetting() {
        return settingRepository.findCurrentlyUsed().orElseThrow(() -> new RuntimeException("No currently used setting"));
    }

    public SendTerrariumCommandDto turnIrradiationOnOff() throws IOException {
        String message;
        LocalTime timeIrradiationStarts = getStartIrradiationTime();
        LocalTime timeIrradiationStops = getEndIrradiationTime();
        if(timeIrradiationStarts.getHour() == LocalTime.now().getHour()) {
            terrariumStateService.changeIrradiation(true);
            message = "on";
        }
        else if(timeIrradiationStops.getHour() == LocalTime.now().getHour()) {
            terrariumStateService.changeIrradiation(true);
            message = "off";
        }
        else message = "null";
        return buildIrradiationMessage(message);
    }

    private SendTerrariumCommandDto buildIrradiationMessage(String message) throws IOException {
        SendTerrariumCommandDto command = new SendTerrariumCommandDto();
        command.setId(27);
        command.setCommand(message);
        String[] cmd = { "bash", "-c", "home/ubuntu/PythonScripts/change_device_mode.py 27 " + message };
        Process p = Runtime.getRuntime().exec(cmd);
        return command;
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
