package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.FilmLike;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmLikesMapper;

@Slf4j
@Component
public class FilmLikesDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmLikesMapper mapper;

    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate, FilmLikesMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public boolean addLike(long k1, long k2) {
        try {

            new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(mapper.getTableName())
                    .execute(
                            mapper.toMap(FilmLike.builder()
                                    .filmId(k1)
                                    .userId(k2)
                                    .build())
                    );
            return true;
        } catch (DuplicateKeyException e) {
            log.warn(
                    "Error! Cannot add user Id: {} like." +
                            " User like already registered for Film Id: {}.",
                    k2, k1
            );
            return false;
        }
    }

    public boolean deleteLike(long k1, long k2) {

        String sqlQuery = "delete from user_film_like where film_id = ? and user_id = ?";
        return jdbcTemplate.update(sqlQuery, k1, k2) > 0;
    }
}