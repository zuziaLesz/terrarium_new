package com.example.smartTerrarium.dto;

import com.sun.jna.platform.win32.Sspi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HistoryDto {
    private Map<Double, LocalDateTime> temperature;
    private Map<Double, LocalDateTime> humidity;
    private Map<Double, LocalDateTime> brightness;
    private Map<Double, LocalDateTime> water;
}
