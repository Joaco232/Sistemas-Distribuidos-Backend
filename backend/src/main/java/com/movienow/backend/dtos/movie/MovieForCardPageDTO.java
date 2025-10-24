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
public class MovieForCardPageDTO {

    private Integer page;

    private List<MovieForCardDTO> results;

    private Integer total_pages;

    private Long total_results;


}