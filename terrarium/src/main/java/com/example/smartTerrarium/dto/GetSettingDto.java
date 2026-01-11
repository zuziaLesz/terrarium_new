package com.example.smartTerrarium.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetSettingDto {
    private Integer id;
    private String name;
    private String description;
    private double temperature;
    private double moisture;
    private double waterOverWeek;
    private LocalTime lightStart;
    private LocalTime lightStop;
    private boolean isCustom;
    private Date lastUpdated;
    private boolean isCurrentlyUsed;
    private int userId;
    private List<String> wateringDays;
    private String wateringMethod;
    private double lightVolume;
    private MultipartFile image;
}
