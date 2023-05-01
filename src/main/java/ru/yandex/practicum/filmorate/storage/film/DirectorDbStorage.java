package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;

@Component
public class DirectorDbStorage extends AbstractDbStorage<Director> {

    protected DirectorDbStorage(JdbcTemplate jdbcTemplate, EntityMapper<Director> mapper) {
        super(jdbcTemplate, mapper);
    }

    public void saveFilmDirector(Long filmId, Set<Director> directors) {
        if (!directors.isEmpty()) {
            directors.forEach(director -> containsOrElseThrow(director.getId()));
            deleteAllFilmDirectors(filmId);
            jdbcTemplate.batchUpdate("INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (?, ?)",
                    directors,
                    100,
                    (PreparedStatement ps, Director director) -> {
                        ps.setLong(1, filmId);
                        ps.setLong(2, director.getId());
                    });
        } else {
            deleteAllFilmDirectors(filmId);
        }
    }

    public Set<Director> findFilmDirector(Long filmId) {
        var sql = "SELECT ID, NAME FROM DIRECTOR WHERE ID IN " +
                "(SELECT DIRECTOR_ID FROM FILM_DIRECTOR WHERE FILM_ID = ?) ORDER BY ID";
        var mapper = new BeanPropertyRowMapper<>(Director.class);
        try {
            return new HashSet<>(jdbcTemplate.query(sql, mapper, filmId));
        } catch (EmptyResultDataAccessException e) {
            return new HashSet<>();
        }
    }

    protected void deleteAllFilmDirectors(Long id) {
        jdbcTemplate.update("DELETE FROM FILM_DIRECTOR WHERE FILM_ID=?", id);
    }

}