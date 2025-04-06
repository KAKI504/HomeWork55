package org.example.labwork55.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.labwork55.dto.UserDto;
import org.example.labwork55.service.QuizResultService;
import org.example.labwork55.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final QuizResultService quizResultService;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/api/users/{userId}/statistics")
    public ResponseEntity<?> getUserStatistics(@PathVariable String userId, Principal principal) {
        if (!principal.getName().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            var results = quizResultService.getResultsByUserEmail(userId);
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalQuizzes", results.size());
            statistics.put("results", results);

            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}