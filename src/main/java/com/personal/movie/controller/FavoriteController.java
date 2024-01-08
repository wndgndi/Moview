package com.personal.movie.controller;

import com.personal.movie.dto.MovieDto;
import com.personal.movie.service.FavoriteService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{id}")
    public ResponseEntity<MovieDto> insertFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.insertFavorite(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getFavorite() {
        return ResponseEntity.ok(favoriteService.getFavorite());
    }
}
