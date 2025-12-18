package com.example.smartTerrarium.service;

import com.example.smartTerrarium.dto.TerrariumDataDto;
import com.example.smartTerrarium.entity.TerrariumData;
import com.example.smartTerrarium.repository.TerrariumDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TerrariumDataService {

    private TerrariumDataRepository terrariumDataRepository;

    @Autowired
    public TerrariumDataService(TerrariumDataRepository terrariumDataRepository) {
        this.terrariumDataRepository = terrariumDataRepository;
    }
    public TerrariumData saveTerrariumData (TerrariumDataDto terrariumDataDto) {
        TerrariumData terrariumData = TerrariumData.builder()
                .temperature(terrariumDataDto.getTemperature())
                .moisture(terrariumDataDto.getMoisture())
                .brightness(terrariumDataDto.getBrightness())
                .lastUpdate(terrariumDataDto.getTimestamp())
                .build();
        return terrariumDataRepository.save(terrariumData);
    }
}
