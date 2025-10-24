package com.movienow.backend.client;


import com.movienow.backend.dtos.movie.MovieDetailsDTO;
import com.movienow.backend.dtos.movie.MovieForSearchPageDTO;
import com.movienow.backend.dtos.provider.ProvidersByMovieDTO;
import com.movienow.backend.dtos.provider.ProvidersListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class MovieApiClient {

    private final RestTemplate restTemplate;

    @Value("${movienow.api.url}")
    private String baseUrl;

    @Value("${movienow.api.key}")
    private String apiKey;

    @Value("${movienow.api.token}")
    private String apiToken;

    public MovieForSearchPageDTO searchMovieByName(String name, Integer page, String language, Boolean include_adult) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", name)
                .queryParam("page", page)
                .queryParam("language", language)
                .queryParam("include_adult", include_adult)
                .encode()
                .toUriString();

        return restTemplate.getForObject(url, MovieForSearchPageDTO.class);
    }

    public MovieDetailsDTO getMovieDetails(Integer movieId, String language) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/movie/" + movieId)
                .queryParam("api_key", apiKey)
                .queryParam("language", language)
                .encode()
                .toUriString();

        return restTemplate.getForObject(url, MovieDetailsDTO.class);
    }

    public ProvidersListDTO getAllMovieProviders(String language, String region) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/watch/providers/movie")
                .queryParam("api_key", apiKey)
                .queryParam("language", language)
                .queryParam("watch_region", region)
                .encode()
                .toUriString();

        return restTemplate.getForObject(url, ProvidersListDTO.class);
    }


    public ProvidersByMovieDTO getWatchProviders(Integer movieId) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/movie/" + movieId + "/watch/providers")
                .queryParam("api_key", apiKey)
                .encode()
                .toUriString();

        return restTemplate.getForObject(url, ProvidersByMovieDTO.class);
    }















}
