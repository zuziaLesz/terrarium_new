package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.RaspberrySetting;
import com.example.smartTerrarium.dto.RaspberryState;
import com.example.smartTerrarium.dto.SendTerrariumCommandDto;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.TerrariumState;
import com.example.smartTerrarium.repository.TerrariumStateRepository;
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
    public void saveState(RaspberryState state) {
        TerrariumState terrariumState = TerrariumState.builder()
                .lastUpdate(new Date())
                .temperature(state.getTemperature())
                .moisture(state.getHumidity())
                .ventilation(false)
                .light(false)
                .build();
        terrariumStateService.save(terrariumState); //CHECK WHAT THE FUCK IS THIS
    }

    public SendTerrariumCommandDto sendHeating(RaspberrySetting raspberryData) {
        SendTerrariumCommandDto command = new SendTerrariumCommandDto();
        command.setId(1);
        if (shouldTurnOnHeating(raspberryData.getTemperature())) {
            command.setCommand("on");
            terrariumStateService.changeHeating(true);
        }
        else if (shouldTurnOffHeating(raspberryData.getTemperature())) {
            command.setCommand("off");
            terrariumStateService.changeHeating(false);
        }
        else command.setCommand(null);
        return command;
    }

    private boolean shouldTurnOnHeating(double temperature) {
        double settingTemperature = settingService.getCurrentSetting().getTemperature();
        return settingTemperature > temperature && !terrariumStateService.getCurrentTerrariumState().isHeating();
    }

    private boolean shouldTurnOffHeating(double temperature) {
        double settingTemperature = settingService.getCurrentSetting().getTemperature();
        return settingTemperature <= temperature && terrariumStateService.getCurrentTerrariumState().isHeating();
    }
}
