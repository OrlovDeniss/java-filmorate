package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.FilmLike;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmLikesMapper implements EntityMapper<FilmLike> {
    private static final String TABLE_NAME = "user_film_like";
    private static final List<String> TABLE_FIELDS = List.of(
            "film_id", "user_id", "like_rate", "is_positive");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(FilmLike filmLike) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), filmLike.getFilmId());
        params.put(TABLE_FIELDS.get(1), filmLike.getUserId());
        params.put(TABLE_FIELDS.get(2), filmLike.getRate());
        params.put(TABLE_FIELDS.get(3), filmLike.getIsPositive());
        return params;
    }

    @Override
    public FilmLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmLike.builder()
                .filmId(rs.getLong(TABLE_FIELDS.get(0)))
                .userId(rs.getLong(TABLE_FIELDS.get(1)))
                .build();
    }
}
