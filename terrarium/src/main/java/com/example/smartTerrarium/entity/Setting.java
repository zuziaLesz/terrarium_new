package com.example.smartTerrarium.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Access(AccessType.FIELD)
@Table(name = "setting")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private double temperature;
    private double moisture;
    @Column(name = "water_over_week")
    private double waterOverWeek;
    private String wateringDays;
    private String wateringMethod;
    @Column(name = "irradiation_start")
    private LocalTime lightStart;
    @Column(name = "irradiation_stop")
    private LocalTime lightStop;
    private double lightVolume;
    @Column(name = "is_custom")
    private boolean isCustom;
    @Column(name = "last_updated")
    private Date lastUpdated;
    @Column(name = "is_currently_used")
    private boolean isCurrentlyUsed;
    @Column(name = "user_id")
    private int userId;
}
