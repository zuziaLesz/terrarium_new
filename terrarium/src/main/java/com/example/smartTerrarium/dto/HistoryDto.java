package com.example.smartTerrarium.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class HistoryDto {
    private Map<Double, LocalDateTime> temperature;
    private Map<Double, LocalDateTime> humidity;
    private Map<Double, LocalDateTime> brightness;
    private Map<Double, LocalDateTime> water;
}
