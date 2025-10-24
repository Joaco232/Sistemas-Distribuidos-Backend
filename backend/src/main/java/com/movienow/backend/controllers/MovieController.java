package com.movienow.backend.controllers;

import com.movienow.backend.dtos.movie.MovieDetailsDTO;
import com.movienow.backend.dtos.movie.MovieForCardPageDTO;
import com.movienow.backend.dtos.movie.MovieForSearchPageDTO;
import com.movienow.backend.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;


    @GetMapping("/name/search")
    public ResponseEntity<MovieForCardPageDTO> getMovieByName(@RequestParam String name,
                                                              @RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "es-MX") String language,
                                                              @RequestParam(defaultValue = "false") Boolean include_adult) {

        MovieForCardPageDTO movieForCardPageDTO = movieService.findMovieByQuery(name, page, language, include_adult, "UY");

        return ResponseEntity.ok(movieForCardPageDTO);
    }





    @GetMapping("/id/search")
    public ResponseEntity<MovieDetailsDTO> getMovieDetailsById(@RequestParam Integer id,
                                                               @RequestParam(defaultValue = "es") String language) {

        return ResponseEntity.ok(movieService.findMovieDetailsById(id, language));
    }








}
