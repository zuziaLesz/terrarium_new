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
    private String deviceName;
    private String type;
    private Integer userId;
    private String groupId;
    private String status;
    private String mode;
    private String lastEditDate;
    private Boolean isRegistered;
}
