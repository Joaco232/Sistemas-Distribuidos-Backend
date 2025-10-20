package com.movienow.backend.dtos.movie;


import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieSearchPageDTO {

    private Integer page;

    private List<MovieForSearchDTO> results;

    private Integer total_pages;

    private Long total_results;


}
