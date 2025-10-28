package com.example.smartTerrarium.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateUserDto {
    private String name;
    private String email;
    private String password;
}
