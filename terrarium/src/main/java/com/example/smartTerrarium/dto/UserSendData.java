package com.example.smartTerrarium.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSendData {
    private String email;
    private String name;
    private String password;
    private String token;
}
