package com.example.smartTerrarium.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class HistoryDto {
    private String timezone;
    private HistoryRangeDto range;
    private List<String> indexes;
    private Map<String, List<Double>> series;
}
