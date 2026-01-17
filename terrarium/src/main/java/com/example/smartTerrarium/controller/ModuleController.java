package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.entity.Module;
import com.example.smartTerrarium.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ModuleController {
    @Autowired
    private final ModuleService moduleService;
    @GetMapping("/module")
    ResponseEntity<List<Module>> getAll() {
        return  ResponseEntity.ok(moduleService.getAllModules());
    }
    @DeleteMapping("/module/{groupId}")
    ResponseEntity<Void> deleteModule(@PathVariable String groupId) {
        moduleService.delete(groupId);
        return ResponseEntity.ok().build();
    }
}
