package org.example.labwork55.controller;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dto.QuestionDto;
import org.example.labwork55.service.QuestionService;
import org.example.labwork55.service.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes/{quizId}/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<?> getQuestionsByQuizId(@PathVariable int quizId) {
        try {
            return ResponseEntity.ok(questionService.getQuestionsByQuizId(quizId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionById(@PathVariable int quizId, @PathVariable int questionId) {
        try {
            QuestionDto question = questionService.getQuestionById(questionId);
            if (question != null) {
                return ResponseEntity.ok(question);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@PathVariable int quizId,
                                            @RequestBody QuestionDto questionDto,
                                            Principal principal) {
        try {
            int questionId = questionService.createQuestion(questionDto, quizId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", questionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getQuestionsByQuizIdPaginated(
            @PathVariable int quizId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(questionService.getQuestionsByQuizIdPaginated(quizId, page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}