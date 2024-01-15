package com.personal.movie.controller;

import com.personal.movie.domain.constants.Category;
import com.personal.movie.dto.request.PostRequest;
import com.personal.movie.dto.response.PostResponse;
import com.personal.movie.service.PostService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestPart("post") PostRequest request,
        @RequestPart("files") List<MultipartFile> multipartFiles) throws IOException {
        return ResponseEntity.ok(postService.createPost(request, multipartFiles));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
        @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponse> deletePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPosts() {
        return ResponseEntity.ok(postService.getMyPosts());
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostResponse>> searchByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(postService.searchByTitle(keyword));
    }

    @GetMapping("/content")
    public ResponseEntity<List<PostResponse>> searchByContent(@RequestParam String keyword) {
        return ResponseEntity.ok(postService.searchByContent(keyword));
    }

    @GetMapping("/writer")
    public ResponseEntity<List<PostResponse>> searchByWriter(@RequestParam String memberName) {
        return ResponseEntity.ok(postService.searchByWriter(memberName));
    }

    @GetMapping("/category")
    public ResponseEntity<List<PostResponse>> searchByCategory(@RequestParam Category category) {
        return ResponseEntity.ok(postService.searchByCategory(category));
    }
}
