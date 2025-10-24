package com.movienow.backend.mappers;

import com.movienow.backend.dtos.provider.ProvidersByMovieDTO;
import com.movienow.backend.dtos.provider.ProvidersListDTO;
import com.movienow.backend.models.Provider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class ProviderMapper {


    public Provider toEntity(ProvidersListDTO.WatchProviderDTO watchProviderDTO) {

        if (watchProviderDTO == null) {
            return null;
        }

        return Provider.builder()
                .idExternal(Long.valueOf(watchProviderDTO.getProvider_id()))
                .name(watchProviderDTO.getProvider_name())
                .logoUrl(watchProviderDTO.getLogo_path())
                .build();
    }



    public List<String> extractProviderName(ProvidersByMovieDTO providersByMovieDTO, String countryCode) {

        if (providersByMovieDTO == null || providersByMovieDTO.getResults() == null) {
            return null;
        }

        return Optional.ofNullable(providersByMovieDTO.getResults().get(countryCode))
                .map(country ->
                        Stream.of(country.getRent(), country.getBuy(), country.getFlatrate())
                                .filter(list -> list != null)
                                .flatMap(list -> list.stream())
                                .filter(providerInfoDTO -> providerInfoDTO != null)
                                .map(providerInfoDTO -> providerInfoDTO.getProvider_name())
                                .filter(name -> name != null && !name.isEmpty())
                                .distinct().toList()
                )
                .orElse(List.of());
    }


}
