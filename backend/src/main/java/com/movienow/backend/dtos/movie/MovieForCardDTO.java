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
public class MovieForCardDTO {

    private Boolean adult;
    private String backdrop_path;
    private Integer id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String title;









}
