package com.personal.movie.controller;

import com.personal.movie.dto.MovieDto;
import com.personal.movie.service.ExternalMovieService;
import com.personal.movie.service.MovieService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class ExternalMovieController {

    private final ExternalMovieService externalMovieService;

    @PostMapping("/popular")
    public ResponseEntity<List<MovieDto>> getPopularMovies() {
        return ResponseEntity.ok(externalMovieService.getPopularMovies());
    }

    @PostMapping("/keyword")
    public ResponseEntity<List<MovieDto>> getMovieByKeyword(@RequestParam String keyword)
        throws IOException {
        return ResponseEntity.ok(externalMovieService.getMovieByKeyword(keyword));
    }

    @PostMapping("/person")
    public ResponseEntity<List<MovieDto>> getMovieByPerson(@RequestParam String personName)
        throws IOException {

        return ResponseEntity.ok(externalMovieService.getMovieByPerson(personName));
    }

}
