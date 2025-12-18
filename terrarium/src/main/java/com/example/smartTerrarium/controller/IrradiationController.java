package com.example.smartTerrarium.controller;

import com.example.smartTerrarium.service.IrradiationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class IrradiationController {

    @Autowired
    private final IrradiationService irradiationService;

}
