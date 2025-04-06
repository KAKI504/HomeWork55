package org.example.labwork55.controller;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dto.QuizDto;
import org.example.labwork55.service.QuizResultService;
import org.example.labwork55.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final QuizResultService quizResultService;

    @GetMapping
    public ResponseEntity<?> getAllQuizzes() {
        try {
            return ResponseEntity.ok(quizService.getAllQuizzes());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuizById(@PathVariable int quizId) {
        try {
            return ResponseEntity.ok(quizService.getQuizById(quizId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizDto quizDto, Principal principal) {
        try {
            int quizId = quizService.createQuiz(quizDto, principal.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", quizId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{quizId}/solve")
    public ResponseEntity<?> solveQuiz(@PathVariable int quizId,
                                       @RequestBody Map<Integer, Integer> answers,
                                       Principal principal) {
        try {
            if (quizResultService.hasUserCompletedQuiz(principal.getName(), quizId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "You have already completed this quiz"));
            }

            Map<String, Boolean> results = quizService.checkQuizAnswers(quizId, answers);

            long correctCount = results.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals("total_questions") && !entry.getKey().equals("total_correct"))
                    .filter(Map.Entry::getValue)
                    .count();

            int totalQuestions = (int) results.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals("total_questions") && !entry.getKey().equals("total_correct"))
                    .count();

            quizResultService.saveResult(principal.getName(), quizId, (int) correctCount, totalQuestions);

            Map<String, Object> response = new HashMap<>();
            response.put("correct", correctCount);
            response.put("total", totalQuestions);
            response.put("results", results);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{quizId}/rate")
    public ResponseEntity<?> rateQuiz(@PathVariable int quizId,
                                      @RequestBody Map<String, Integer> rateRequest,
                                      Principal principal) {
        try {
            Integer score = rateRequest.get("score");
            if (score == null || score < 1 || score > 5) {
                return ResponseEntity.badRequest().body(Map.of("error", "Score must be between 1 and 5"));
            }

            Map<String, Object> result = quizResultService.rateQuiz(principal.getName(), quizId, score);

            if (result.containsKey("error")) {
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{quizId}/results")
    public ResponseEntity<?> getQuizResults(@PathVariable int quizId) {
        try {
            return ResponseEntity.ok(quizResultService.getResultsByQuizId(quizId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{quizId}/leaderboard")
    public ResponseEntity<?> getLeaderboard(@PathVariable int quizId,
                                            @RequestParam(defaultValue = "10") int limit) {
        try {
            return ResponseEntity.ok(quizResultService.getLeaderboard(quizId, limit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}