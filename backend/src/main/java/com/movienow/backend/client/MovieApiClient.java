package com.movienow.backend.client;


import com.movienow.backend.dtos.movie.MovieSearchPageDTO;
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


    public MovieSearchPageDTO searchMovieByName(String name, Integer page, String language, Boolean include_adult) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", name)
                .queryParam("page", page)
                .queryParam("language", language)
                .queryParam("include_adult", include_adult)
                .encode()
                .toUriString();

        return restTemplate.getForObject(url, MovieSearchPageDTO.class);
    }










}
