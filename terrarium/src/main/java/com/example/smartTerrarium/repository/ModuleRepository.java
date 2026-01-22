package com.example.smartTerrarium.repository;

import com.example.smartTerrarium.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    @Query(value = "SELECT * FROM module WHERE user_id = :userId ", nativeQuery = true)
    Optional<List<Module>> findModulesByUser(@Param("userId") int userId);

    @Query(value = "SELECT * FROM module WHERE group_id = :groupId LIMIT 1", nativeQuery = true)
    Optional<Module> findFirstByGroupId(@Param("groupId") String groupId);

    List<Module> getAllByGroupId(String groupId);

}
