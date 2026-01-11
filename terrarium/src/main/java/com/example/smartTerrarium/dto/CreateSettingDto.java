package com.example.smartTerrarium.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateSettingDto {
    private String name;
    private String description;
    private Double temperature;
    private Double moisture;
    private Double waterOverWeek;
    private LocalTime lightStart;
    private LocalTime lightStop;
    private String wateringMethod;
    private Double lightVolume;
    private List<String> wateringDays;
    private MultipartFile image;
}
