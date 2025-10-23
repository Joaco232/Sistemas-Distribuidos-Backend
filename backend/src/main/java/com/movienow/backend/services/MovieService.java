package com.movienow.backend.services;

import com.movienow.backend.client.MovieApiClient;
import com.movienow.backend.dtos.movie.MovieSearchPageDTO;
import com.movienow.backend.models.Movie;
import com.movienow.backend.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieApiClient movieApiClient;


    public MovieSearchPageDTO findMovieByQuery(String query, Integer page, String language, Boolean include_adult) {

        return movieApiClient.searchMovieByName(query, page, language, include_adult);
    }




}
