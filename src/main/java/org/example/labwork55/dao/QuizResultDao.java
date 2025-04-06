package org.example.labwork55.dao;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.model.QuizResult;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class QuizResultDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public List<QuizResult> getResultsByQuizId(int quizId) {
        String sql = "select * from quiz_results where quiz_id = ? order by score desc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), quizId);
    }

    public List<QuizResult> getResultsByUserId(String userId) {
        String sql = "select * from quiz_results where user_id = ? order by completed_at desc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), userId);
    }

    public boolean hasUserCompletedQuiz(String userId, int quizId) {
        String sql = "select count(*) from quiz_results where user_id = ? and quiz_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);
        return count != null && count > 0;
    }

    public int saveResult(QuizResult result) {
        String sql = "insert into quiz_results(user_id, quiz_id, score, total_questions, completed_at) values(?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, result.getUserId());
                    ps.setInt(2, result.getQuizId());
                    ps.setInt(3, result.getScore());
                    ps.setInt(4, result.getTotalQuestions());
                    ps.setTimestamp(5, Timestamp.valueOf(result.getCompletedAt()));
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<QuizResult> getLeaderboardForQuiz(int quizId, int limit) {
        String sql = "select * from quiz_results where quiz_id = ? order by score desc, completed_at asc limit ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), quizId, limit);
    }
}