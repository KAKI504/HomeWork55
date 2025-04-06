package org.example.labwork55.service;

import org.example.labwork55.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> getQuestionsByQuizId(int quizId);
    QuestionDto getQuestionById(int id);
    int createQuestion(QuestionDto questionDto, int quizId);
    List<QuestionDto> getQuestionsByQuizIdPaginated(int quizId, int page, int size);
}