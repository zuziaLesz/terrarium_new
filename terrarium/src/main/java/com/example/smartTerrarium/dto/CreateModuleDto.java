package com.example.smartTerrarium.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class CreateModuleDto {
    private String device_name;
    private String type;
    private Integer user_id;
    private String group_id;
    private String status;
    private String mode;
    private String last_edit_date;
    private Boolean is_registered;
    private String water_min;
    private String water_max;
    private Double intensity;
}
