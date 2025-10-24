package com.movienow.backend.services;

import com.movienow.backend.client.MovieApiClient;
import com.movienow.backend.dtos.provider.ProvidersByMovieDTO;
import com.movienow.backend.dtos.provider.ProvidersListDTO;
import com.movienow.backend.mappers.ProviderMapper;
import com.movienow.backend.models.Provider;
import com.movienow.backend.repositories.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final MovieApiClient movieApiClient;
    private final ProviderMapper providerMapper;
    private final ProviderRepository providerRepository;

    public void saveProvidersFrom() {

        ProvidersListDTO providersListDTO = movieApiClient.getAllMovieProviders("es", "UY");

        List<ProvidersListDTO.WatchProviderDTO> providers = providersListDTO.getResults();

        List<Provider> providerEntities = providers.stream()
                .map(providerDTO -> providerMapper.toEntity(providerDTO))
                .toList();

        providerRepository.saveAll(providerEntities);

    }




}
