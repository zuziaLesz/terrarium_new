package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.CreateModuleDto;
import com.example.smartTerrarium.dto.DeleteModuleDto;
import com.example.smartTerrarium.entity.Module;
import com.example.smartTerrarium.exception.ModuleException;
import com.example.smartTerrarium.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ModuleService {
    @Autowired
    private final ModuleRepository moduleRepository;
    private final UserService userService;
    private final WebClient webClient;
    public void add(List<CreateModuleDto> createModules) {
        List<Module> modules = createModules.stream()
                .map(this::mapDtoToModule)
                .collect(Collectors.toList());

        moduleRepository.saveAll(modules);
    }
    public List<Module> getAllModules() {
        return moduleRepository.findModulesByUser(userService.getCurrentUser().getId()).orElse(List.of());
    }
    private Module mapDtoToModule(CreateModuleDto dto) {

        if (Boolean.FALSE.equals(dto.getIs_registered()) || dto.getUser_id() == null) {
            throw new ModuleException("Module is not registered!");
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Module.builder()
                .deviceName(dto.getDevice_name())
                .type(dto.getType())
                .userId(dto.getUser_id())
                .status(dto.getState())
                .mode(dto.getMode())
                .groupId(dto.getGroup_id())
                .lastEditDate(LocalDateTime.parse(dto.getLast_edit_date(), fmt))
                .isRegistered(dto.getIs_registered())
                .intensity(dto.getIntensity())
                .build();
    }
    public void delete(String groupId) {
        List<Module> modules = moduleRepository.getAllByGroupId(groupId);
        DeleteModuleDto deleteModuleDto = DeleteModuleDto.builder()
                .groupId(groupId)
                .build();
        sendDeleteMessage(deleteModuleDto);
        moduleRepository.deleteAll(modules);
    }
    //groupId - nic nie wysylane - wysylac tylko group id
    public void sendDeleteMessage(DeleteModuleDto deleteModuleDto) {
        System.out.println("deleteing Module");
        webClient.post()
                .uri("https://api.leafcore.eu/api/external/devices/unregistered/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deleteModuleDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
