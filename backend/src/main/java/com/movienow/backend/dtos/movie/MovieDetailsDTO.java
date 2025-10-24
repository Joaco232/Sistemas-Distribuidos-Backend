package com.movienow.backend.dtos.movie;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailsDTO {

    private Long id;
    private String title;
    private String overview;
    private String release_date;
    private Integer runtime;
    private String poster_path;
    private List<LanguageDTO> spoken_languages;
    private List<GenreDTO> genres;

    @Data
    public static class LanguageDTO {
        private String english_name;
        private String iso_639_1;
        private String name;
    }

    @Data
    public static class GenreDTO {
        private Long id;
        private String name;
    }

}
