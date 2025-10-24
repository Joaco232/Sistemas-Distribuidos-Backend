package com.movienow.backend.services;

import com.movienow.backend.client.MovieApiClient;
import com.movienow.backend.dtos.movie.MovieDetailsDTO;
import com.movienow.backend.dtos.movie.MovieForCardDTO;
import com.movienow.backend.dtos.movie.MovieForCardPageDTO;
import com.movienow.backend.dtos.movie.MovieForSearchPageDTO;
import com.movienow.backend.mappers.MovieMapper;
import com.movienow.backend.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieApiClient movieApiClient;
    private final MovieMapper movieMapper;


    public MovieForCardPageDTO findMovieByQuery(String query, Integer page, String language, Boolean include_adult, String countryCode) {

        MovieForSearchPageDTO movieForSearchPageDTO = movieApiClient.searchMovieByName(query, page, language, include_adult);

        return movieMapper.mapToCardPage(movieForSearchPageDTO, countryCode);
    }

    public MovieDetailsDTO findMovieDetailsById(Integer id, String language) {

        return movieApiClient.getMovieDetails(id, language);
    }









}
