package com.personal.movie.controller;

import com.personal.movie.dto.MovieDto;
import com.personal.movie.service.HistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getHistory() {
        return ResponseEntity.ok(historyService.getHistory());
    }

    @DeleteMapping("/{historyId}")
    public ResponseEntity<String> deleteHistory(@PathVariable Long historyId) {
        historyService.deleteHistory(historyId);
        return ResponseEntity.ok("히스토리가 삭제되었습니다.");
    }
}
