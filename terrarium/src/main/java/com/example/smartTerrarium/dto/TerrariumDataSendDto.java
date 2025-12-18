package com.example.smartTerrarium.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TerrariumDataSendDto {
    private String setting_id;
    private String plant_name;
    private Float optimal_temperature;
    private Float optimal_humidity;
    private Float optimal_brightness;
    private LocalTime light_schedule_start_time;
    private LocalTime light_schedule_end_time;
    private String watering_mode;
    private int water_amount;
    private Float light_intensity;
    private List<String> DayOfWeek;
}
