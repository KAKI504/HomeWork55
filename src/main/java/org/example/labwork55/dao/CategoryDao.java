package org.example.labwork55.dao;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.model.Category;
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
public class CategoryDao {
    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public List<Category> getAllCategories() {
        String sql = "select * from categories";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

    public Optional<Category> getCategoryById(int id) {
        String sql = "select * from categories where id = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class), id)
                )
        );
    }

    public int createCategory(Category category) {
        String sql = "insert into categories(name) values(?)";

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, category.getName());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void assignCategoryToQuiz(int quizId, int categoryId) {
        String sql = "insert into quiz_categories(quiz_id, category_id) values(?, ?)";
        jdbcTemplate.update(sql, quizId, categoryId);
    }

    public List<Category> getCategoriesByQuizId(int quizId) {
        String sql = "select c.* from categories c " +
                "join quiz_categories qc on c.id = qc.category_id " +
                "where qc.quiz_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class), quizId);
    }
}