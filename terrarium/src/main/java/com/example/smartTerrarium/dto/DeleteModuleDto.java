package com.example.smartTerrarium.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Getter
public class DeleteModuleDto {
    private String device_name;
    private String user_id;
    private boolean is_registered;

}
