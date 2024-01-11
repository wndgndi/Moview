package com.personal.movie.controller;

import com.personal.movie.dto.request.ReviewRequest;
import com.personal.movie.dto.response.ReviewResponse;
import com.personal.movie.service.ReviewService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ReviewResponse> createReview(
        @RequestPart("review") @Valid ReviewRequest request,
        @RequestPart("movie") Long id,
        @RequestPart("files") List<MultipartFile> multipartFiles) throws IOException {
        return ResponseEntity.ok(reviewService.createReview(request, id, multipartFiles));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReview(reviewId));

    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long reviewId,
        @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, request));

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }
}
