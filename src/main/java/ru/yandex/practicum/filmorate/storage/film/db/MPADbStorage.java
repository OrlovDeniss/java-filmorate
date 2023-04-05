package ru.yandex.practicum.filmorate.storage.film.db;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.film.db.mapper.MPAMapper;

@Component
public class MPADbStorage extends AbstractDbStorage<MPARating> {

    protected MPADbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, new MPAMapper());
    }

    protected void saveFilmMpa(Long filmId, Long mpa) {
        deleteAllFilmMpa(filmId);
        jdbcTemplate.update("INSERT INTO FILM_MPA (FILM_ID, MPA_ID) VALUES (?, ?)", filmId, mpa);
    }

    protected MPARating findFilmMpa(Long id) {
        var sql = "SELECT ID, NAME FROM MPA_RATING WHERE ID IN " +
                "(SELECT MPA_ID FROM FILM_MPA WHERE FILM_ID = ?)";
        var mapper = new BeanPropertyRowMapper<>(MPARating.class);
        try {
            return jdbcTemplate.queryForObject(sql, mapper, id);
        } catch (EmptyResultDataAccessException e) {
            return new MPARating();
        }
    }

    protected void deleteAllFilmMpa(Long id) {
        jdbcTemplate.update("DELETE FROM FILM_MPA WHERE FILM_ID=?", id);
    }
}
