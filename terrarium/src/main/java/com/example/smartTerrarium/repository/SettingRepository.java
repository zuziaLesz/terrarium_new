package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {

    @Query(value = "SELECT * FROM setting WHERE is_currently_used = TRUE LIMIT 1", nativeQuery = true)
    Optional<Setting> findCurrentlyUsed();
}
