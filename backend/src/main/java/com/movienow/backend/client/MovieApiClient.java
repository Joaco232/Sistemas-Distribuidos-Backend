package com.movienow.backend.client;


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


    public String searchMovieByName(String name) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", name)
                .encode()
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }










}
