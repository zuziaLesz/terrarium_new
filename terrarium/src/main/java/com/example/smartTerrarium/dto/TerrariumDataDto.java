package com.example.smartTerrarium.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TerrariumDataDto {
    private Double temperature;
    private Double humidity;
    private Double brightness;
    private String water_min;
    private String water_max;
    private LocalDateTime timestamp;
}
