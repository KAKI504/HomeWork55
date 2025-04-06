package org.example.labwork55.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dao.OptionDao;
import org.example.labwork55.dao.QuestionDao;
import org.example.labwork55.dao.QuizDao;
import org.example.labwork55.dto.CategoryDto;
import org.example.labwork55.dto.OptionDto;
import org.example.labwork55.dto.QuestionDto;
import org.example.labwork55.dto.QuizDto;
import org.example.labwork55.exceptions.QuizNotFoundException;
import org.example.labwork55.model.Option;
import org.example.labwork55.model.Question;
import org.example.labwork55.model.Quiz;
import org.example.labwork55.service.CategoryService;
import org.example.labwork55.service.QuizService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final OptionDao optionDao;
    private final CategoryService categoryService;

    @Override
    public List<QuizDto> getAllQuizzes() {
        List<Quiz> quizzes = quizDao.getAllQuizzes();
        return quizzes.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuizDto getQuizById(int id) {
        Quiz quiz = quizDao.getQuizById(id)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + id));

        List<CategoryDto> categories = categoryService.getCategoriesByQuizId(id);

        QuizDto quizDto = mapToDto(quiz);
        quizDto.setCategories(categories);

        return quizDto;
    }

    @Override
    public int createQuiz(QuizDto quizDto, String creatorEmail) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setCreatedBy(creatorEmail);
        quiz.setTimeLimitSeconds(quizDto.getTimeLimitSeconds() != null ? quizDto.getTimeLimitSeconds() : 0);

        int quizId = quizDao.createQuiz(quiz);

        if (quizDto.getCategories() != null && !quizDto.getCategories().isEmpty()) {
            for (CategoryDto categoryDto : quizDto.getCategories()) {
                if (categoryDto.getId() != null) {
                    categoryService.assignCategoryToQuiz(quizId, categoryDto.getId());
                } else if (categoryDto.getName() != null && !categoryDto.getName().trim().isEmpty()) {
                    int categoryId = categoryService.createCategory(categoryDto);
                    categoryService.assignCategoryToQuiz(quizId, categoryId);
                }
            }
        }

        if (quizDto.getQuestions() != null) {
            for (QuestionDto questionDto : quizDto.getQuestions()) {
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
            }
        }

        return quizId;
    }

    @Override
    public Map<String, Boolean> checkQuizAnswers(int quizId, Map<Integer, Integer> userAnswers) {
        Map<String, Boolean> results = new HashMap<>();

        List<Question> questions = questionDao.getQuestionsByQuizId(quizId);

        int correctAnswers = 0;

        for (Question question : questions) {
            Integer selectedOptionId = userAnswers.get(question.getId());

            if (selectedOptionId == null) {
                results.put("question_" + question.getId(), false);
                continue;
            }

            List<Option> correctOptions = optionDao.getCorrectOptionsByQuestionId(question.getId());

            boolean isCorrect = correctOptions.stream()
                    .anyMatch(option -> option.getId() == selectedOptionId);

            results.put("question_" + question.getId(), isCorrect);

            if (isCorrect) {
                correctAnswers++;
            }
        }

        results.put("total_questions", true);
        results.put("total_correct", true);

        return results;
    }

    private QuizDto mapToDto(Quiz quiz) {
        List<Question> questions = questionDao.getQuestionsByQuizId(quiz.getId());

        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questions) {
            List<Option> options = optionDao.getOptionsByQuestionId(question.getId());

            List<OptionDto> optionDtos = options.stream()
                    .map(option -> OptionDto.builder()
                            .id(option.getId())
                            .optionText(option.getOptionText())
                            .isCorrect(null)
                            .build())
                    .collect(Collectors.toList());

            questionDtos.add(QuestionDto.builder()
                    .id(question.getId())
                    .questionText(question.getQuestionText())
                    .options(optionDtos)
                    .build());
        }

        return QuizDto.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .timeLimitSeconds(quiz.getTimeLimitSeconds())
                .questions(questionDtos)
                .build();
    }
}