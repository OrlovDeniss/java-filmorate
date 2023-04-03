package ru.yandex.practicum.filmorate.storage.film.db.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmMapper implements EntityMapper<Film> {

    private String TABLE_NAME = "film";
    private List<String> TABLE_FIELDS = List.of(
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
        Map<String, Object> params = new HashMap<>();
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
            film.setName(rs.getString(TABLE_FIELDS.get(0)));
            film.setDescription(rs.getString(TABLE_FIELDS.get(1)));
            film.setReleaseDate(rs.getDate(TABLE_FIELDS.get(2)).toLocalDate());
            film.setDuration(rs.getInt(TABLE_FIELDS.get(3)));
        } catch (SQLException e) {
            throw new FilmNotFoundException();
        }
        return film;
    }
}
