package org.example.labwork55.service;

import org.example.labwork55.dto.QuizDto;

import java.util.List;
import java.util.Map;

public interface QuizService {
    List<QuizDto> getAllQuizzes();
    QuizDto getQuizById(int id);
    int createQuiz(QuizDto quizDto, String creatorEmail);
    Map<String, Boolean> checkQuizAnswers(int quizId, Map<Integer, Integer> userAnswers);
}