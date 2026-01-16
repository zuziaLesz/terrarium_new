package com.example.smartTerrarium.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class GetPredefinedSettingDto {
    private Integer id;
    private String name;
    private String description;
    private double temperature;
    private double moisture;
    private double waterOverWeek;
    private LocalTime lightStart;
    private LocalTime lightStop;
    private List<String> wateringDays;
    private String wateringMethod;
    private double lightVolume;
    private byte[] image;
}
