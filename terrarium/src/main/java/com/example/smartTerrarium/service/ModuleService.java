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

@RequiredArgsConstructor
@Service
public class ModuleService {
    @Autowired
    private final ModuleRepository moduleRepository;
    private final UserService userService;
    private final WebClient webClient;
    public void add(CreateModuleDto createModuleDto) {
        moduleRepository.save(mapDtoToModule(createModuleDto));
    }
    public List<Module> getAllModules() {
        return moduleRepository.findModulesByUser(userService.getCurrentUser().getId()).orElse(List.of());
    }
    private Module mapDtoToModule(CreateModuleDto dto) {

        if (Boolean.FALSE.equals(dto.getIsRegistered()) || dto.getUserId() == null) {
            throw new ModuleException("Module is not registered!");
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return Module.builder()
                .deviceName(dto.getDeviceName())
                .type(dto.getType())
                .userId(dto.getUserId())
                .status(dto.getStatus())
                .mode(dto.getMode())
                .groupId(dto.getGroupId())
                .lastEditDate(LocalDateTime.parse(dto.getLastEditDate(), fmt))
                .isRegistered(dto.getIsRegistered())
                .build();
    }
    public void delete(String groupId) {
        List<Module> modules = moduleRepository.getAllByGroupId(groupId);
        DeleteModuleDto deleteModuleDto = DeleteModuleDto.builder()
                .device_name(groupId)
                .user_id(null)
                .is_registered(false)
                .build();
        sendDeleteMessage(deleteModuleDto);
        moduleRepository.deleteAll(modules);
    }
    public void sendDeleteMessage(DeleteModuleDto deleteModuleDto) {
        System.out.println("deleteing Module");
        webClient.post()
                .uri("https://leafcore.eu/api/external/devices/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deleteModuleDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
