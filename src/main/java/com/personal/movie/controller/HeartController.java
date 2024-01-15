package com.personal.movie.controller;

import com.personal.movie.dto.HeartDto;
import com.personal.movie.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/{postId}")
    public ResponseEntity<HeartDto> heart(@PathVariable Long postId) {
        return ResponseEntity.ok(heartService.heart(postId));
    }
}
