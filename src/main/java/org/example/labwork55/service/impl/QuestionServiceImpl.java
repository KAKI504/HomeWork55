package org.example.labwork55.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dao.OptionDao;
import org.example.labwork55.dao.QuestionDao;
import org.example.labwork55.dto.OptionDto;
import org.example.labwork55.dto.QuestionDto;
import org.example.labwork55.exceptions.QuestionNotFoundException;
import org.example.labwork55.model.Option;
import org.example.labwork55.model.Question;
import org.example.labwork55.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;
    private final OptionDao optionDao;

    @Override
    public List<QuestionDto> getQuestionsByQuizId(int quizId) {
        List<Question> questions = questionDao.getQuestionsByQuizId(quizId);
        return questions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDto getQuestionById(int id) {
        Question question = questionDao.getQuestionById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + id));
        return mapToDto(question);
    }

    @Override
    public int createQuestion(QuestionDto questionDto, int quizId) {
        Question question = new Question();
        question.setQuizId(quizId);
        question.setQuestionText(questionDto.getQuestionText());

        int questionId = questionDao.createQuestion(question);

        if (questionDto.getOptions() != null) {
            for (OptionDto optionDto : questionDto.getOptions()) {
                Option option = new Option();
                option.setQuestionId(questionId);
                option.setOptionText(optionDto.getOptionText());
                option.setCorrect(optionDto.getIsCorrect());
                optionDao.createOption(option);
            }
        }

        return questionId;
    }

    private QuestionDto mapToDto(Question question) {
        List<Option> options = optionDao.getOptionsByQuestionId(question.getId());

        List<OptionDto> optionDtos = options.stream()
                .map(option -> OptionDto.builder()
                        .id(option.getId())
                        .optionText(option.getOptionText())
                        .isCorrect(null)
                        .build())
                .collect(Collectors.toList());

        return QuestionDto.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .options(optionDtos)
                .build();
    }
}