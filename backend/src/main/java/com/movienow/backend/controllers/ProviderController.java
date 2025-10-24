package com.movienow.backend.controllers;


import com.movienow.backend.services.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;


    @PostMapping("/save")
    public void saveProviders() {
        providerService.saveProvidersFrom();
    }


}
