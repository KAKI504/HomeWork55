package org.example.labwork55.dao;

import lombok.RequiredArgsConstructor;
import org.example.labwork55.model.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final KeyHolder keyHolder = new GeneratedKeyHolder();

    public List<User> getUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public Optional<User> getUserById(int id) {
        String sql = "select * from users where id = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id)
                )
        );
    }

    public Optional<User> getUserByEmail(String email) {
        String sql = "select * from users where email = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(
                        jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email)
                )
        );
    }

    public void create(User user) {
        String sql = "insert into users(username, email, password, enabled, role_id) " +
                "values(:username, :email, :password, :enabled, :roleId)";
        namedParameterJdbcTemplate.update(
                sql,
                new MapSqlParameterSource()
                        .addValue("username", user.getUsername())
                        .addValue("email", user.getEmail())
                        .addValue("password", user.getPassword())
                        .addValue("enabled", user.isEnabled())
                        .addValue("roleId", user.getRoleId())
        );
    }

    public int createAndReturnId(User user) {
        String sql = "insert into users(username, email, password, enabled, role_id) " +
                "values(?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setBoolean(4, user.isEnabled());
                    ps.setInt(5, user.getRoleId());
                    return ps;
                }, keyHolder
        );
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }
}