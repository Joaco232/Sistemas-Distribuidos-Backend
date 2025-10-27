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

    private static final String TMDB_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String DEFAULT_SIZE = "w300";


    public void saveProvidersFrom() {

        ProvidersListDTO providersListDTO = movieApiClient.getAllMovieProviders("es", "UY");

        List<ProvidersListDTO.WatchProviderDTO> providers = providersListDTO.getResults();

        List<Provider> providerEntities = providers.stream()
                .map(providerDTO -> providerMapper.toEntity(providerDTO))
                .toList();

        providerRepository.saveAll(providerEntities);

    }

    public List<Provider> getAllPlatforms() {
        List<Provider> providers = providerRepository.findAll();

        return providers.stream()
            .map(provider -> {
                String logoPath = provider.getLogoUrl();

                if (logoPath != null && !logoPath.isEmpty()) {
                    String cleanPath = logoPath;
                    if (logoPath.startsWith("/")) {
                        cleanPath = logoPath.substring(1);
                    }
                    String fullUrl = TMDB_BASE_URL + DEFAULT_SIZE + "/" + cleanPath;
                    provider.setLogoUrl(fullUrl);
                }

                return provider;
            })
            .toList();
    }




}




