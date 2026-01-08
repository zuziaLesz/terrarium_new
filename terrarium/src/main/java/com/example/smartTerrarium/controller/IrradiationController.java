package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.service.LightScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class IrradiationController {

    @Autowired
    private final LightScheduler irradiationService;

}
