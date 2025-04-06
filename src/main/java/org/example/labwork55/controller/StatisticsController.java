package org.example.labwork55.controller;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserStatistics(@PathVariable String userId, Principal principal) {
        if (!principal.getName().equals(userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
        }

        try {
            Map<String, Object> statistics = statisticsService.getUserStatistics(userId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/global")
    public ResponseEntity<?> getGlobalStatistics() {
        try {
            Map<String, Object> statistics = statisticsService.getGlobalStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}