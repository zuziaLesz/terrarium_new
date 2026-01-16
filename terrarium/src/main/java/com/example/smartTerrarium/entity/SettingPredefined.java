package com.example.smartTerrarium.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Access(AccessType.FIELD)
@Table(name = "pre_setting")
public class SettingPredefined {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Double temperature;
    private Double moisture;
    private LocalTime light_start;
    private LocalTime light_end;
    private Double light_volume;
    private Double water_amount;
    private String watering_method;
    private String watering_days;
    private byte[] image;
}
