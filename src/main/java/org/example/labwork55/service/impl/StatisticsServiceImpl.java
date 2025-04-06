package org.example.labwork55.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.dao.QuizDao;
import org.example.labwork55.dao.QuizResultDao;
import org.example.labwork55.dao.UserDao;
import org.example.labwork55.dto.QuizResultDto;
import org.example.labwork55.model.QuizResult;
import org.example.labwork55.service.QuizResultService;
import org.example.labwork55.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final UserDao userDao;
    private final QuizResultDao quizResultDao;
    private final QuizDao quizDao;
    private final QuizResultService quizResultService;

    @Override
    public Map<String, Object> getUserStatistics(String userEmail) {
        Map<String, Object> statistics = new HashMap<>();

        List<QuizResult> results = quizResultDao.getResultsByUserId(userEmail);

        statistics.put("totalQuizzes", results.size());
        statistics.put("totalQuizzesAvailable", quizDao.getAllQuizzes().size());

        OptionalDouble avgScore = results.stream()
                .mapToDouble(r -> ((double) r.getScore() / r.getTotalQuestions()) * 100)
                .average();
        statistics.put("averageScore", avgScore.orElse(0.0));

        OptionalDouble bestScore = results.stream()
                .mapToDouble(r -> ((double) r.getScore() / r.getTotalQuestions()) * 100)
                .max();
        statistics.put("bestScore", bestScore.orElse(0.0));

        List<QuizResultDto> resultDtos = quizResultService.getResultsByUserEmail(userEmail);
        statistics.put("results", resultDtos);

        statistics.put("averageCompletionTimeMinutes", 0);

        return statistics;
    }

    @Override
    public Map<String, Object> getGlobalStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        int totalUsers = userDao.getUsers().size();
        int totalQuizzes = quizDao.getAllQuizzes().size();

        statistics.put("totalUsers", totalUsers);
        statistics.put("totalQuizzes", totalQuizzes);

        List<QuizResult> allResults = quizResultDao.getAllResults();

        statistics.put("totalAttempts", allResults.size());

        OptionalDouble globalAvgScore = allResults.stream()
                .mapToDouble(r -> ((double) r.getScore() / r.getTotalQuestions()) * 100)
                .average();
        statistics.put("globalAverageScore", globalAvgScore.orElse(0.0));

        Map<String, Long> topUsers = allResults.stream()
                .collect(Collectors.groupingBy(QuizResult::getUserId, Collectors.counting()));

        statistics.put("topUsers", topUsers.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        return statistics;
    }
}