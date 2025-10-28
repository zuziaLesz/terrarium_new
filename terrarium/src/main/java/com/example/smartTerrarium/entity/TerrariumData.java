package com.example.smartTerrarium.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

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
    private Date lastUpdate;
    private double temperature;
    private double moisture;
}
