package com.example.smartTerrarium.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DashboardDto {
    private Double temperature;
    private Double humidity;
    private Double brightness;
    private LocalTime lightStart;
    private LocalTime lightEnd;
    private Timestamp timeUntilWatering;
    private String name;
    private String description;
}
