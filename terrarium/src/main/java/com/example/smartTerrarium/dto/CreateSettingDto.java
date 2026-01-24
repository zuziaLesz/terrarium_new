package com.example.smartTerrarium.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
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
