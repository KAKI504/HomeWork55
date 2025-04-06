package org.example.labwork55.controller;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.service.QuizResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class QuizResultController {
    private final QuizResultService quizResultService;

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getQuizResults(@PathVariable int quizId) {
        try {
            return ResponseEntity.ok(quizResultService.getResultsByQuizId(quizId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/quiz/{quizId}/leaderboard")
    public ResponseEntity<?> getLeaderboard(@PathVariable int quizId,
                                            @RequestParam(defaultValue = "10") int limit) {
        try {
            return ResponseEntity.ok(quizResultService.getLeaderboard(quizId, limit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserResults(@PathVariable String userId, Principal principal) {
        if (!principal.getName().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }
        try {
            return ResponseEntity.ok(quizResultService.getResultsByUserEmail(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}