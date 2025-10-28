package com.example.smartTerrarium.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TerrariumDataDto {
    private double temperature;
    private double moisture;
}
