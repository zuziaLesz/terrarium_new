package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.TerrariumData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerrariumDataRepository extends JpaRepository<TerrariumData, Integer> {
}
