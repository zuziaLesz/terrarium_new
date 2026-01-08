package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.TerrariumData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TerrariumDataRepository extends JpaRepository<TerrariumData, Integer> {
    @Query(value = "SELECT * FROM terrarium_data ORDER BY last_update DESC LIMIT 1", nativeQuery = true)
    Optional<TerrariumData> findMostRecent();
}
