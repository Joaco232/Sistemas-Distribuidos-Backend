package com.movienow.backend.controllers;

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
    public ResponseEntity<String> getMovieByName(@RequestParam String name) {

        return ResponseEntity.ok(movieService.findMovieByQuery(name));
    }








}
