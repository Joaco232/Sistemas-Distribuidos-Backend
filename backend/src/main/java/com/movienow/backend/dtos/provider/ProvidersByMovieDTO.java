package com.movienow.backend.dtos.provider;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProvidersByMovieDTO {

    private Long id;
    private Map<String, CountryProvidersDTO> results;

    @Data
    public static class CountryProvidersDTO {
        private String link;
        private List<ProviderInfoDTO> flatrate;
        private List<ProviderInfoDTO> rent;
        private List<ProviderInfoDTO> buy;
    }

    @Data
    public static class ProviderInfoDTO {
        private String logo_path;
        private Integer provider_id;
        private String provider_name;
        private Integer display_priority;
    }
}