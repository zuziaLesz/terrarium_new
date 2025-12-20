package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {

    @Query(value = "SELECT * FROM setting WHERE is_currently_used = TRUE AND user_id = :userId LIMIT 1", nativeQuery = true)
    Optional<Setting> findCurrentlyUsed(@Param("userId") int userId);

    List<Setting> findAllByUserId(int userId);
}
