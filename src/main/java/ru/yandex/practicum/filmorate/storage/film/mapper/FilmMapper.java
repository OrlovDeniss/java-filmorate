package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class FilmMapper implements EntityMapper<Film> {

    private static final String TABLE_NAME = "film";
    private static final List<String> TABLE_FIELDS = List.of(
            "name",
            "description",
            "release",
            "duration");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(Film film) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), film.getName());
        params.put(TABLE_FIELDS.get(1), film.getDescription());
        params.put(TABLE_FIELDS.get(2), film.getReleaseDate());
        params.put(TABLE_FIELDS.get(3), film.getDuration());
        return params;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) {
        Film film = new Film();
        try {
            film.setId(rs.getLong("id"));
            String name = rs.getString(TABLE_FIELDS.get(0));
            if (Objects.nonNull(name)) {
                film.setName(rs.getString(TABLE_FIELDS.get(0)));
            }
            String description = rs.getString(TABLE_FIELDS.get(1));
            if (Objects.nonNull(description)) {
                film.setDescription(description);
            }
            Date releaseDate = rs.getDate(TABLE_FIELDS.get(2));
            if (Objects.nonNull(releaseDate)) {
                film.setReleaseDate(releaseDate.toLocalDate());
            }
            film.setDuration(rs.getInt(TABLE_FIELDS.get(3)));
            film.setRate(rs.getInt("rate"));
        } catch (SQLException e) {
            throw new FilmNotFoundException("Фильм не найден.");
        }
        return film;
    }
}
