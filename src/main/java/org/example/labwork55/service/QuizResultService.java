package org.example.labwork55.service;

import org.example.labwork55.dto.QuizResultDto;

import java.util.List;
import java.util.Map;

public interface QuizResultService {
    QuizResultDto saveResult(String userEmail, int quizId, int score, int totalQuestions);
    List<QuizResultDto> getResultsByQuizId(int quizId);
    List<QuizResultDto> getResultsByUserEmail(String userEmail);
    List<QuizResultDto> getLeaderboard(int quizId, int limit);
    boolean hasUserCompletedQuiz(String userEmail, int quizId);
    Map<String, Object> rateQuiz(String userEmail, int quizId, int score);
    List<QuizResultDto> getResultsByQuizIdPaginated(int quizId, int page, int size);

}