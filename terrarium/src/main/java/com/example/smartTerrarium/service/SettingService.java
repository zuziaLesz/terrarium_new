package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.CreateSettingDto;
import com.example.smartTerrarium.dto.GetSettingDto;
import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.exception.NoCurrentSettingException;
import com.example.smartTerrarium.exception.SettingDoesNotExistException;
import com.example.smartTerrarium.exception.SettingIsNotCustomException;
import com.example.smartTerrarium.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SettingService {
    private SettingRepository settingRepository;
    private UserService userService;
    private TerrariumDataService terrariumDataService;

    @Autowired
    public SettingService(SettingRepository settingRepository, UserService userService, TerrariumDataService terrariumDataService) {
        this.settingRepository = settingRepository;
        this.userService = userService;
        this.terrariumDataService = terrariumDataService;
    }
    public void createSetting(CreateSettingDto createSettingDto) {
        Setting setting = buildSettingFromCreateSetting(createSettingDto);
        settingRepository.save(setting);
        changeCurrentSetting(setting.getId());
    }

    public void editSetting(Integer id, CreateSettingDto createSettingDto) {
        Setting setting = getSettingById(id);
        if(!setting.isCustom()) {
            throw new SettingIsNotCustomException("You cannot edit setting you did not make");
        }
        if(createSettingDto.getName() != null && !createSettingDto.getName().isEmpty()) {
            setting.setName(createSettingDto.getName());
        }
        if(createSettingDto.getDescription() != null && !createSettingDto.getDescription().isEmpty()) {
            setting.setDescription(createSettingDto.getDescription());
        }
        if(createSettingDto.getTemperature() != null) {
            setting.setTemperature(createSettingDto.getTemperature());
        }
        if(createSettingDto.getMoisture() != null) {
            setting.setMoisture(createSettingDto.getMoisture());
        }
        if(createSettingDto.getWaterOverWeek() != null) {
            setting.setWaterOverWeek(createSettingDto.getWaterOverWeek());
        }
        if(createSettingDto.getLightStart() != null) {
            setting.setLightStart(createSettingDto.getLightStart());
        }
        if(createSettingDto.getLightStop() != null) {
            setting.setLightStart(createSettingDto.getLightStop());
        }
        if(createSettingDto.getWateringMethod() != null && !createSettingDto.getWateringMethod().isEmpty()) {
            setting.setWateringMethod(createSettingDto.getWateringMethod());
        }
        if(createSettingDto.getLightVolume() != null) {
            setting.setLightVolume(createSettingDto.getLightVolume());
        }
        setting.setLastUpdated(new Date());
        setting.setUserId(userService.getCurrentUser().getId());
        if(createSettingDto.getWateringDays() != null && !createSettingDto.getWateringDays().isEmpty()) {
            setting.setWateringDays(mapWateringDaysToString(createSettingDto.getWateringDays()));
        }
        settingRepository.save(setting);
    }

    public List<GetSettingDto> getAllSettings() {
        int userId = userService.getCurrentUser().getId();
        List<Setting> settingList = settingRepository.findAllByUserId(userId);
        return settingList.stream()
                .map(this::mapSettingToDto)
                .collect(Collectors.toList());
    }

    public GetSettingDto getSetting(Integer id) {
        Setting setting = getSettingById(id);
        return mapSettingToDto(setting);
    }

    public void deleteSetting(Integer id) {
        Setting setting = getSettingById(id);
        if(!setting.isCustom()) {
            throw new SettingIsNotCustomException("You cannot delete setting you did not make");
        }
        settingRepository.deleteById(id);
    }
    public Setting getSettingById(Integer id) {
        return settingRepository.findById(id)
                .orElseThrow(() -> new SettingDoesNotExistException(id));
    }

    public TerrariumDataDto applySetting(Integer id) {
        changeCurrentSetting(id);
        Setting setting = getCurrentSetting();
        terrariumDataService.sendTerariumData(terrariumDataService.mapSettingToTerrariumDataSend(setting));
        return TerrariumDataDto.builder()
                .temperature(setting.getTemperature())
                .moisture(setting.getMoisture())
                .build();
    }

    private void changeCurrentSetting(Integer id) {
        if(doesCurrentSettingExist()) {
            Setting previousSetting = getCurrentSetting();
            previousSetting.setCurrentlyUsed(false);
            settingRepository.save(previousSetting);
        }
        Setting setting = getSettingById(id);
        setting.setCurrentlyUsed(true);
        settingRepository.save(setting);
    }
    private Setting buildSettingFromCreateSetting(CreateSettingDto createSettingDto) {
        Setting setting = Setting.builder()
                .name(createSettingDto.getName())
                .description(createSettingDto.getDescription())
                .temperature(createSettingDto.getTemperature())
                .moisture(createSettingDto.getMoisture())
                .waterOverWeek(createSettingDto.getWaterOverWeek())
                .lightStart(createSettingDto.getLightStart())
                .lightStop(createSettingDto.getLightStop())
                .wateringMethod(createSettingDto.getWateringMethod())
                .lightVolume(createSettingDto.getLightVolume())
                .isCustom(true)
                .lastUpdated(new Date())
                .isCurrentlyUsed(false)
                .userId(userService.getCurrentUser().getId())
                .build();
        setting.setWateringDays(mapWateringDaysToString(createSettingDto.getWateringDays()));
        return setting;
    }

    public GetSettingDto getCurrentSettingAndMap() {
        Setting setting = getCurrentSetting();
        return mapSettingToDto(setting);

    }
    public Setting getCurrentSetting() {
        int currentUserId = userService.getCurrentUser().getId();
        return settingRepository.findCurrentlyUsed(currentUserId).orElseThrow(NoCurrentSettingException::new);
    }



    private GetSettingDto mapSettingToDto(Setting setting) {
        GetSettingDto settingDto = GetSettingDto.builder()
                .id(setting.getId())
                .name(setting.getName())
                .description(setting.getDescription())
                .temperature(setting.getTemperature())
                .moisture(setting.getMoisture())
                .waterOverWeek(setting.getWaterOverWeek())
                .lightStart(setting.getLightStart())
                .lightStop(setting.getLightStop())
                .isCustom(setting.isCustom())
                .lastUpdated(setting.getLastUpdated())
                .isCurrentlyUsed(setting.isCurrentlyUsed())
                .userId(setting.getUserId())
                .wateringMethod(setting.getWateringMethod())
                .lightVolume(setting.getLightVolume())
                .build();
        settingDto.setWateringDays(mapWateringDaysToList(setting.getWateringDays()));
        return settingDto;
    }

    private boolean doesCurrentSettingExist() {
        int currentUserId = userService.getCurrentUser().getId();
        Optional<Setting> currentSetting = settingRepository.findCurrentlyUsed(currentUserId);
        return currentSetting.isPresent();
    }

    private String mapWateringDaysToString(List<String> listOfDays) {
        return listOfDays.stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(","));
    }

    public List<String> mapWateringDaysToList(String listOfDays) {
        return Arrays.stream(listOfDays.split(","))
                .collect(Collectors.toList());
    }
}
