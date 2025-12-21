package com.example.smartTerrarium.dto;

import lombok.Getter;

@Getter
public class CreateModuleDto {
    private String name;
    private String type;
    private String parentId;
    private String status;
}
