package com.example.smartTerrarium.dto;

import com.sun.jna.platform.win32.Sspi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TerrariumDataDto {
    private double temperature;
    private double moisture;
    private double brightness;
    private LocalDateTime timestamp;
}
