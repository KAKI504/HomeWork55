package org.example.labwork55.dao;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.model.Option;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OptionDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public List<Option> getOptionsByQuestionId(int questionId) {
        String sql = "select * from options where question_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Option.class), questionId);
    }

    public int createOption(Option option) {
        String sql = "insert into options(question_id, option_text, is_correct) values(?, ?, ?)";

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setInt(1, option.getQuestionId());
                    ps.setString(2, option.getOptionText());
                    ps.setBoolean(3, option.isCorrect());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public List<Option> getCorrectOptionsByQuestionId(int questionId) {
        String sql = "select * from options where question_id = ? and is_correct = true";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Option.class), questionId);
    }
}