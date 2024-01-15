package com.personal.movie.controller;

import com.personal.movie.dto.CommentDto;
import com.personal.movie.service.CommentService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto request,
        @PathVariable Long postId) {
        return ResponseEntity.ok(commentService.createComment(request, postId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@RequestBody CommentDto request,
        @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.updateComment(request, commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDto> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}
