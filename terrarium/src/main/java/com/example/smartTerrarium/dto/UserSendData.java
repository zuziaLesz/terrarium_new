package com.example.smartTerrarium.dto;

import lombok.Builder;

@Builder
public class UserSendData {
    private String email;
    private String name;
    private String password;
    private String token;
}
