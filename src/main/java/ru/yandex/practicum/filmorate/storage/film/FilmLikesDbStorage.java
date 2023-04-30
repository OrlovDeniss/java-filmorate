package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.model.film.FilmLike;
import ru.yandex.practicum.filmorate.storage.film.mapper.FilmLikesMapper;

import java.util.Optional;

@Slf4j
@Component
public class FilmLikesDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmLikesMapper mapper;

    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate, FilmLikesMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public Long addLike(long k1, long k2) {
        try {

            return new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(mapper.getTableName())
                    .usingGeneratedKeyColumns("id")
                    .executeAndReturnKey(mapper.toMap(
                            FilmLike.builder().filmId(k1).userId(k2).build()))
                    .longValue();

        } catch (DuplicateKeyException e) {
            log.warn(
                    "Error! Cannot add user Id: {} like." +
                            " User like already registered for Film Id: {}.",
                    k2, k1
            );
            return null;
        }
    }

    public Long deleteLike(long k1, long k2) {
        Optional<FilmLike> optV = findLike(k1, k2);
        if (optV.isPresent()) {
            String sqlQuery = "delete from user_film_like where film_id = ? and user_id = ?";
            jdbcTemplate.update(sqlQuery, k1, k2);
        }
        return optV.map(AbstractEntity::getId).orElse(null);
    }

    protected Optional<FilmLike> findLike(long k1, long k2) {
        try {
            var sql = "select * from " + mapper.getTableName() +
                    " where film_id = ? and user_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, k1, k2));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}