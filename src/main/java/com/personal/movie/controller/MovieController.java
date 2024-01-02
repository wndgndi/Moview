package com.personal.movie.controller;

import com.personal.movie.dto.MovieDto;
import com.personal.movie.service.MovieService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @Value("${movie.secret}")
    private String apiKey;

    @PostMapping
    public ResponseEntity<MovieDto> insertMovie(@RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.insertMovie(movieDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id,
        @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieDto> deleteMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }

    @PostMapping("/popular")
    public ResponseEntity<List<MovieDto>> insertPopularMovies() {
        return ResponseEntity.ok(movieService.getPopularMovies(apiKey));
    }

    @PostMapping("/keyword")
    public ResponseEntity<List<MovieDto>> getMovieByKeyword(@RequestParam String keyword)
        throws IOException {
        return ResponseEntity.ok(movieService.getMovieByKeyword(apiKey, keyword));
    }

    @PostMapping("/person")
    public ResponseEntity<List<MovieDto>> getMovieByPerson(@RequestParam String personName)
        throws IOException {

        return ResponseEntity.ok(movieService.getMovieByPerson(apiKey, personName));
    }

}
