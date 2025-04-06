package org.example.labwork55.dao;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.model.Question;
import org.example.labwork55.model.QuizResult;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class QuestionDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public List<Question> getQuestionsByQuizId(int quizId) {
        String sql = "select * from questions where quiz_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Question.class), quizId);
    }

    public Optional<Question> getQuestionById(int id) {
        String sql = "select * from questions where id = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Question.class), id)
                )
        );
    }

    public int createQuestion(Question question) {
        String sql = "insert into questions(quiz_id, question_text) values(?, ?)";

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setInt(1, question.getQuizId());
                    ps.setString(2, question.getQuestionText());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
    public List<Question> getQuestionsByQuizIdPaginated(int quizId, int page, int size) {
        int offset = page * size;
        String sql = "select * from questions where quiz_id = ? limit ? offset ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Question.class), quizId, size, offset);
    }

    public List<QuizResult> getResultsByQuizIdPaginated(int quizId, int page, int size) {
        int offset = page * size;
        String sql = "select * from quiz_results where quiz_id = ? order by score desc limit ? offset ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResult.class), quizId, size, offset);
    }
}