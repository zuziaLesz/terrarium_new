package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.dto.TerrariumDataSendDto;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.repository.TerrariumDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TerrariumDataService {

    private TerrariumDataRepository terrariumDataRepository;
    private final WebClient webClient;

    @Autowired
    public TerrariumDataService(TerrariumDataRepository terrariumDataRepository, WebClient webClient) {
        this.terrariumDataRepository = terrariumDataRepository;
        this.webClient = webClient;
    }
    public List<TerrariumData> saveTerrariumData (List<TerrariumDataDto> terrariumDataDto) {
        List<TerrariumData> terrariumDataList = terrariumDataDto.stream()
                .map(dto -> TerrariumData.builder()
                        .temperature(dto.getTemperature())
                        .moisture(dto.getMoisture())
                        .brightness(dto.getBrightness())
                        .lastUpdate(dto.getTimestamp())
                        .build()
                )
                .toList();

        return terrariumDataRepository.saveAll(terrariumDataList);
    }
    public void sendTerariumData(TerrariumDataSendDto data) {
            webClient.post()
                    .uri("http://172.19.240.152:5000/current-setting")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(data)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
    }
}
