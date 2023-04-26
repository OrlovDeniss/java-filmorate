package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;

@Component
public class LikesDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected void saveFilmLikes(Long filmId, Set<Long> likes) {
        if (likes.isEmpty()) {
            jdbcTemplate.update("INSERT INTO USER_FILM_LIKE (FILM_ID) VALUES (?)", filmId);
        } else {
            jdbcTemplate.batchUpdate("INSERT INTO USER_FILM_LIKE (FILM_ID, USER_ID) VALUES (?, ?)",
                    likes,
                    100,
                    (PreparedStatement ps, Long like) -> {
                        ps.setLong(1, filmId);
                        ps.setLong(2, like);
                    });
        }
    }

    protected Set<Long> findFilmLikes(Long id) {
        var sql = "SELECT USER_ID FROM USER_FILM_LIKE " +
                "WHERE FILM_ID = ? AND USER_ID IS NOT NULL";
        try {
            return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong(1), id));
        } catch (EmptyResultDataAccessException e) {
            return new HashSet<>();
        }
    }

}