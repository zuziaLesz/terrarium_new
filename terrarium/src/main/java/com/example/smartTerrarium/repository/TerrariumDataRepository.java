package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.TerrariumData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TerrariumDataRepository extends JpaRepository<TerrariumData, Integer> {
    @Query(value = "SELECT * FROM terrarium_data  WHERE user_id = :userId ORDER BY last_update DESC LIMIT 1", nativeQuery = true)
    Optional<TerrariumData> findMostRecent(@Param("userId") Integer userId);
    Optional<List<TerrariumData>> findAllByPlantIdAndLastUpdateAfter(Integer plantId, LocalDateTime from);
}
