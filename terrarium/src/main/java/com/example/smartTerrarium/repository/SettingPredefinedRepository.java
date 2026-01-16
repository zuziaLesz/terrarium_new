package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.SettingPredefined;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingPredefinedRepository extends JpaRepository<SettingPredefined, Integer> {
}
