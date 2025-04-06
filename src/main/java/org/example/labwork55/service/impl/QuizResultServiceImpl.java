package org.example.labwork55.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dao.QuizDao;
import org.example.labwork55.dao.QuizResultDao;
import org.example.labwork55.dao.UserDao;
import org.example.labwork55.dto.QuizResultDto;
import org.example.labwork55.model.Quiz;
import org.example.labwork55.model.QuizResult;
import org.example.labwork55.model.User;
import org.example.labwork55.service.QuizResultService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizResultServiceImpl implements QuizResultService {
    private final QuizResultDao quizResultDao;
    private final UserDao userDao;
    private final QuizDao quizDao;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public QuizResultDto saveResult(String userEmail, int quizId, int score, int totalQuestions) {
        QuizResult result = new QuizResult();
        result.setUserId(userEmail);
        result.setQuizId(quizId);
        result.setScore(score);
        result.setTotalQuestions(totalQuestions);
        result.setCompletedAt(LocalDateTime.now());

        int resultId = quizResultDao.saveResult(result);
        result.setId(resultId);

        return mapToDto(result);
    }

    @Override
    public List<QuizResultDto> getResultsByQuizId(int quizId) {
        return quizResultDao.getResultsByQuizId(quizId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuizResultDto> getResultsByUserEmail(String userEmail) {
        return quizResultDao.getResultsByUserId(userEmail).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuizResultDto> getLeaderboard(int quizId, int limit) {
        return quizResultDao.getLeaderboardForQuiz(quizId, limit).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasUserCompletedQuiz(String userEmail, int quizId) {
        return quizResultDao.hasUserCompletedQuiz(userEmail, quizId);
    }

    @Override
    public Map<String, Object> rateQuiz(String userEmail, int quizId, int score) {
        Map<String, Object> response = new HashMap<>();

        if (hasUserCompletedQuiz(userEmail, quizId)) {
            response.put("error", "User has already completed this quiz");
            return response;
        }

        Quiz quiz = quizDao.getQuizById(quizId).orElse(null);
        if (quiz == null) {
            response.put("error", "Quiz not found");
            return response;
        }

        int totalQuestions = 0;
        QuizResultDto resultDto = saveResult(userEmail, quizId, score, totalQuestions);

        response.put("success", true);
        response.put("result", resultDto);
        return response;
    }

    private QuizResultDto mapToDto(QuizResult result) {
        User user = userDao.getUserByEmail(result.getUserId()).orElse(null);
        Quiz quiz = quizDao.getQuizById(result.getQuizId()).orElse(null);

        return QuizResultDto.builder()
                .id(result.getId())
                .username(user != null ? user.getUsername() : result.getUserId())
                .quizId(result.getQuizId())
                .quizTitle(quiz != null ? quiz.getTitle() : "Unknown")
                .score(result.getScore())
                .totalQuestions(result.getTotalQuestions())
                .completedAt(result.getCompletedAt().format(formatter))
                .build();
    }
}