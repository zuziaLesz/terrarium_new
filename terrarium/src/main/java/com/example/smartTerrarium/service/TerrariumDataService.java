package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.dto.TerrariumDataSendDto;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.repository.SettingRepository;
import com.example.smartTerrarium.repository.TerrariumDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TerrariumDataService {

    private TerrariumDataRepository terrariumDataRepository;
    private final UserService userService;
    private final SettingService settingService;

    @Autowired
    public TerrariumDataService(TerrariumDataRepository terrariumDataRepository, UserService userService, SettingService settingService) {
        this.terrariumDataRepository = terrariumDataRepository;
        this.userService = userService;
        this.settingService = settingService;
    }
    public List<TerrariumData> saveTerrariumData (List<TerrariumDataDto> terrariumDataDto) {
        Setting setting = settingService.getCurrentSetting();
        List<TerrariumData> terrariumDataList = terrariumDataDto.stream()
                .map(dto -> TerrariumData.builder()
                        .temperature(dto.getTemperature())
                        .moisture(dto.getMoisture())
                        .brightness(dto.getBrightness())
                        .lastUpdate(dto.getTimestamp())
                        .userId(userService.getCurrentUser().getId())
                        .plantId(setting.getId())
                        .build()
                )
                .toList();

        return terrariumDataRepository.saveAll(terrariumDataList);
    }
    private List<String> mapWteringDaysToList(String days) {
        return Arrays.stream(days.split(","))
                .collect(Collectors.toList());
    }
}
