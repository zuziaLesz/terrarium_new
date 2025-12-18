package com.example.smartTerrarium.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TerrariumStateDto {
    private Integer id;
    private LocalDateTime lastUpdate;
    private double temperature;
    private double moisture;
    private boolean ventilation;
    private boolean irradiation;
}
