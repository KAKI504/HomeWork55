package org.example.labwork55.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDto {
    private Integer id;
    private String username;
    private Integer quizId;
    private String quizTitle;
    private Integer score;
    private Integer totalQuestions;
    private String completedAt;
}