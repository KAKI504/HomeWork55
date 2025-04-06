package org.example.labwork55.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResult {
    private int id;
    private String userId;
    private int quizId;
    private int score;
    private int totalQuestions;
    private LocalDateTime completedAt;
}