package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.film.mapper.GenreMapper;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class GenreDbStorage extends AbstractDbStorage<Genre> {

    @Override
    public Optional<Genre> delete(Long id) {
        throw new MethodNotImplemented("Метод не реализован");
    }

    protected GenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, new GenreMapper());
    }

    protected void saveFilmGenres(Long filmId, Set<Genre> genres) {
        if (!genres.isEmpty()) {
            genres.forEach(genre -> containsOrElseThrow(genre.getId()));
            deleteAllFilmGenres(filmId);
            jdbcTemplate.batchUpdate("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)",
                    genres,
                    100,
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genre.getId());
                    });
        } else {
            deleteAllFilmGenres(filmId);
        }
    }

    protected Set<Genre> findFilmGenres(Long id) {
        var sql = "SELECT ID, NAME FROM GENRE WHERE ID IN " +
                "(SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?) ORDER BY ID";
        var mapper = new BeanPropertyRowMapper<>(Genre.class);
        try {
            return new HashSet<>(jdbcTemplate.query(sql, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return new HashSet<>();
        }
    }

    protected void deleteAllFilmGenres(Long id) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID=?", id);
    }
}
