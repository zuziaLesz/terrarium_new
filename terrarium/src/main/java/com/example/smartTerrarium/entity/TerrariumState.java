package com.example.smartTerrarium.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "terrarium_state")
@Builder
@Setter
public class TerrariumState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    private double temperature;
    private double moisture;
    private boolean ventilation;
    private boolean light;
    private boolean heating;
}
