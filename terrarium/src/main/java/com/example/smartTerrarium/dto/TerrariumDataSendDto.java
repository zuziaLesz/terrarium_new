package com.example.smartTerrarium.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TerrariumDataSendDto {
    private String setting_id;
    private String plant_name;
    private Float optimal_temperature;
    private Float optimal_humidity;
    private Float optimal_brightness;
    private String light_schedule_start_time;
    private String light_schedule_end_time;
    private String watering_mode;
    private Integer water_amount;
    private Float light_intensity;
    private List<String> DayOfWeek;
}
