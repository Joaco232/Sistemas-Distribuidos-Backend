package com.movienow.backend.dtos.provider;


import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProvidersListDTO {

    private List<WatchProviderDTO> results;

    @Data
    public static class WatchProviderDTO {
        private Map<String, Integer> display_priorities;
        private String logo_path;
        private String provider_name;
        private Integer provider_id;
    }

}
