package com.movienow.backend.mappers;


import com.movienow.backend.client.MovieApiClient;
import com.movienow.backend.dtos.movie.MovieForCardDTO;
import com.movienow.backend.dtos.movie.MovieForCardPageDTO;
import com.movienow.backend.dtos.movie.MovieForSearchDTO;
import com.movienow.backend.dtos.movie.MovieForSearchPageDTO;
import com.movienow.backend.dtos.provider.ProvidersByMovieDTO;
import com.movienow.backend.enums.Genre;
import com.movienow.backend.models.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieMapper {

    private final ProviderMapper providerMapper;
    private final MovieApiClient movieApiClient;

    public MovieForCardPageDTO mapToCardPage(MovieForSearchPageDTO movieForSearchPageDTO, String countryCode) {

        if (movieForSearchPageDTO == null) {

            return null;
        }

        return MovieForCardPageDTO.builder()
                .page(movieForSearchPageDTO.getPage())
                .total_pages(movieForSearchPageDTO.getTotal_pages())
                .total_results(movieForSearchPageDTO.getTotal_results())
                .results(mapToCardsDTO(movieForSearchPageDTO, countryCode))
                .build();
    }


    public List<MovieForCardDTO> mapToCardsDTO(MovieForSearchPageDTO movieForSearchPageDTO, String countryCode) {

        if (movieForSearchPageDTO == null || movieForSearchPageDTO.getResults() == null) {

            return null;
        }

        return movieForSearchPageDTO.getResults().stream()
                .map(movieForSearchDTO -> mapToMovieForCard(movieForSearchDTO, countryCode))
                .toList();


    }

    public MovieForCardDTO mapToMovieForCard(MovieForSearchDTO movieForSearchDTO, String countryCode) {

        if (movieForSearchDTO == null) {

            return null;
        }

        MovieForCardDTO movieForCardDTO = MovieForCardDTO.builder()
                .adult(movieForSearchDTO.getAdult())
                .original_language(movieForSearchDTO.getOriginal_language())
                .original_title(movieForSearchDTO.getOriginal_title())
                .id(movieForSearchDTO.getId())
                .title(movieForSearchDTO.getTitle())
                .backdrop_path(movieForSearchDTO.getBackdrop_path())
                .release_date(movieForSearchDTO.getRelease_date())
                .overview(movieForSearchDTO.getOverview())
                .build();


        ProvidersByMovieDTO providersByMovieDTO = movieApiClient.getWatchProviders(movieForSearchDTO.getId());
        List<String> providers = providerMapper.extractProviderName(providersByMovieDTO, countryCode);
        movieForCardDTO.setPlatforms(providers);

        movieForCardDTO.setGenres(movieForSearchDTO.getGenre_ids().stream()
                .filter(id -> id != null)
                .map(id -> Genre.fromId(id))
                .filter(genre -> genre != null)
                .map(genre -> genre.getNombre())
                .collect(Collectors.toList())
        );

        return movieForCardDTO;
    }








}
