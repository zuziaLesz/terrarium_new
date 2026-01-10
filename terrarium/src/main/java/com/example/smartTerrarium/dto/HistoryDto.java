package com.example.smartTerrarium.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HistoryDto {
    private String timezone;
    private HistoryRangeDto range;
    private List<String> indexes;
    private Map<String, List<Double>> series;
}
