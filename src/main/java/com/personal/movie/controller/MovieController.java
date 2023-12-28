package com.personal.movie.controller;

import com.personal.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @Value("${movie.secret}")
    private String apiKey;

    @PostMapping("/popular")
    public void getPopularMovies() {
        movieService.getPopularMovie(apiKey);
    }
}
