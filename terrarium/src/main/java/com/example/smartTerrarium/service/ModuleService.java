package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.CreateModuleDto;
import com.example.smartTerrarium.entity.Module;
import com.example.smartTerrarium.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ModuleService {
    @Autowired
    private final ModuleRepository moduleRepository;
    private final UserService userService;
    public void add(CreateModuleDto createModuleDto) {
        moduleRepository.save(mapDtoToModule(createModuleDto));
    }
    public List<Module> getAllModules() {
        return moduleRepository.findModulesByUser(userService.getCurrentUser().getId()).orElse(List.of());
    }
    private Module mapDtoToModule(CreateModuleDto createModuleDto) {
      return Module.builder()
              .name(createModuleDto.getName())
              .type(createModuleDto.getType())
              .status(createModuleDto.getStatus())
              .parentId(createModuleDto.getParentId())
              .userId(userService.getCurrentUser().getId())
              .build();
    }
}
