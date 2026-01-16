package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.*;
import com.example.smartTerrarium.entity.Setting;
import com.example.smartTerrarium.entity.SettingPredefined;
import com.example.smartTerrarium.exception.NoCurrentSettingException;
import com.example.smartTerrarium.exception.SettingDoesNotExistException;
import com.example.smartTerrarium.exception.SettingIsNotCustomException;
import com.example.smartTerrarium.repository.SettingPredefinedRepository;
import com.example.smartTerrarium.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
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
    private WebClient webClient;
    private SettingPredefinedRepository settingPredefinedRepository;

    @Autowired
    public SettingService(SettingRepository settingRepository, UserService userService, WebClient webClient, SettingPredefinedRepository settingPredefinedRepository) {
        this.settingRepository = settingRepository;
        this.userService = userService;
        this.webClient = webClient;
        this.settingPredefinedRepository = settingPredefinedRepository;
    }
    public void createSetting(CreateSettingDto createSettingDto) throws IOException {
        Setting setting = buildSettingFromCreateSetting(createSettingDto);
        settingRepository.save(setting);
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
        sendTerariumData(mapSettingToTerrariumDataSend(setting));
        settingRepository.save(setting);
    }

    public void editImage(MultipartFile image, Integer settingId) throws IOException {
        Setting setting = getSettingById(settingId);
        if(!setting.isCustom()) {
            throw new SettingIsNotCustomException("You cannot edit setting you did not make");
        }
        else {
            setting.setImage(image.getBytes());
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

    public List<GetPredefinedSettingDto> getAllPredefinedSettings() {
        List<SettingPredefined> settingList = settingPredefinedRepository.findAll();
        return settingList.stream()
                .map(this::mapPredefinedSettingToDto)
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
        sendTerariumData(mapSettingToTerrariumDataSend(setting));
        return TerrariumDataDto.builder()
                .temperature(setting.getTemperature())
                .moisture(setting.getMoisture())
                .build();
    }
    public void addPredefined(Integer id) {
        SettingPredefined preSetting = settingPredefinedRepository.findById(id).orElseThrow(() -> new SettingDoesNotExistException(id));
        Integer userId = userService.getCurrentUser().getId();
        Setting setting = mapPredefinedSettingToEntity(preSetting, userId);
        settingRepository.save(setting);
    }
    public void save(Setting setting) {
        settingRepository.save(setting);
    }
    public TerrariumDataSendDto mapSettingToTerrariumDataSend(Setting setting) {
        return TerrariumDataSendDto.builder()
                .setting_id(setting.getId().toString())
                .plant_name(setting.getName())
                .optimal_temperature(setting.getTemperature().floatValue())
                .optimal_humidity(setting.getMoisture().floatValue())
                .optimal_brightness(setting.getLightVolume().floatValue())
                .light_schedule_start_time(setting.getLightStart().toString())
                .light_schedule_end_time(setting.getLightStop().toString())
                .watering_mode(setting.getWateringMethod())
                .water_amount(setting.getWaterOverWeek().intValue())
                .light_intensity(setting.getLightVolume().floatValue())
                .DayOfWeek(mapWateringDaysToList(setting.getWateringDays()))
                .build();
    }

    public void sendTerariumData(TerrariumDataSendDto data) {
        webClient.post()
                .uri("https://api.leafcore.eu/external/settings")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data)
                .retrieve()
                .toBodilessEntity()
                .block();
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
    private Setting buildSettingFromCreateSetting(CreateSettingDto createSettingDto) throws IOException {
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
        if(createSettingDto.getImage() != null) {
            setting.setImage(createSettingDto.getImage().getBytes());
        }
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

    public Setting getCurrentSettingByUserId(Integer userId) {
        return settingRepository.findCurrentlyUsed(userId).orElseThrow(NoCurrentSettingException::new);
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
                .image(setting.getImage())
                .build();
        settingDto.setWateringDays(mapWateringDaysToList(setting.getWateringDays()));
        return settingDto;
    }

    private boolean doesCurrentSettingExist() {
        int currentUserId = userService.getCurrentUser().getId();
        Optional<Setting> currentSetting = settingRepository.findCurrentlyUsed(currentUserId);
        return currentSetting.isPresent();
    }

    public String mapWateringDaysToString(List<String> listOfDays) {
        return listOfDays.stream()
                .map(String::toUpperCase)
                .collect(Collectors.joining(","));
    }

    public List<String> mapWateringDaysToList(String listOfDays) {
        return Arrays.stream(listOfDays.split(","))
                .collect(Collectors.toList());
    }
    private GetPredefinedSettingDto mapPredefinedSettingToDto(SettingPredefined setting) {
        GetPredefinedSettingDto dto = GetPredefinedSettingDto.builder()
                .id(setting.getId())
                .name(setting.getName())
                .description(setting.getDescription())
                .temperature(setting.getTemperature())
                .moisture(setting.getMoisture())
                .lightStart(setting.getLight_start())
                .lightStop(setting.getLight_end())
                .wateringMethod(setting.getWatering_method())
                .lightVolume(setting.getLight_volume())
                .build();
        dto.setWateringDays(mapWateringDaysToList(setting.getWatering_days()));
        return dto;
    }
    private Setting mapPredefinedSettingToEntity(SettingPredefined predefined, Integer userId) {
        return Setting.builder()
                .name(predefined.getName())
                .description(predefined.getDescription())
                .temperature(predefined.getTemperature())
                .moisture(predefined.getMoisture())
                .waterOverWeek(predefined.getWater_amount())
                .wateringDays(predefined.getWatering_days())
                .wateringMethod(predefined.getWatering_method())
                .lightStart(predefined.getLight_start())
                .lightStop(predefined.getLight_end())
                .lightVolume(predefined.getLight_volume())
                .isCustom(true)
                .lastUpdated(new Date())
                .userId(userId)
                .isCurrentlyUsed(false)
                .image(predefined.getImage())
                .build();
    }
}
