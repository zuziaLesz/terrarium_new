package com.example.smartTerrarium.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Access(AccessType.FIELD)
@Table(name = "Module")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "device_name")
    private String deviceName;
    private String type;
    @Column(name = "user_id")
    private int userId;
    private String status;
    private String mode;
    @Column(name = "is_registered")
    private boolean isRegistered;
    @Column(name = "group_id")
    private String groupId;
    @Column(name = "last_edit_date")
    private LocalDateTime lastEditDate;
    private Double intensity;
}
