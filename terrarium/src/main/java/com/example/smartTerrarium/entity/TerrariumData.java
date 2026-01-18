package com.example.smartTerrarium.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "terrarium_data")
public class TerrariumData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "last_update")
    private LocalDateTime timestamp;
    private double temperature;
    private double moisture;
    private double brightness;
    @Column(name="user_id")
    private Integer userId;
    @Column(name="plant_id")
    private Integer plantId;
    private String waterLevel;
}
