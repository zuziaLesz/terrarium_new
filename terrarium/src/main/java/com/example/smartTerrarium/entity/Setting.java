package com.example.smartTerrarium.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

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
    private Double temperature;
    private Double moisture;
    @Column(name = "water_over_week")
    private Double waterOverWeek;
    @Column(name = "watering_days")
    private String wateringDays;
    @Column(name = "watering_method")
    private String wateringMethod;
    @Column(name = "irradiation_start")
    private LocalTime lightStart;
    @Column(name = "irradiation_stop")
    private LocalTime lightStop;
    @Column(name = "light_volume")
    private Double lightVolume;
    @Column(name = "is_custom")
    private boolean isCustom;
    @Column(name = "last_updated")
    private Date lastUpdated;
    @Column(name = "is_currently_used")
    private boolean isCurrentlyUsed;
    @Column(name = "user_id")
    private int userId;
}
