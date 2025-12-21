package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.Module;
import com.example.smartTerrarium.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    @Query(value = "SELECT * FROM Module WHERE user_id = :userId ", nativeQuery = true)
    Optional<List<Module>> findModulesByUser(@Param("userId") int userId);
}
