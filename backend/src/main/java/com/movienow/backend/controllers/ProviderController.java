package com.movienow.backend.controllers;


import com.movienow.backend.dtos.ApiResponse;
import com.movienow.backend.mappers.ApiResponseMapper;
import com.movienow.backend.models.Provider;
import com.movienow.backend.services.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    private final ApiResponseMapper apiResponseMapper;


    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveProviders() {

        providerService.saveProvidersFrom();
        return apiResponseMapper.makeResponseEntity(HttpStatus.OK, "Todas las plataformas cargadas con exito");

    }

    @GetMapping("/all")
    public ResponseEntity<List<Provider>> getAllProviders(){
        return new ResponseEntity<>(providerService.getAllPlatforms(), HttpStatus.OK);
    }


}
