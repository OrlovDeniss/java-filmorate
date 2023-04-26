package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GenreMapper implements EntityMapper<Genre> {

    private static final String TABLE_NAME = "genre";
    private static final List<String> TABLE_FIELDS = List.of("name");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(Genre genre) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), genre.getName());
        return params;
    }

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) {
        Genre genre = new Genre();
        try {
            genre.setId(rs.getLong("id"));
            genre.setName(rs.getString(TABLE_FIELDS.get(0)));
        } catch (SQLException e) {
            throw new EntityNotFoundException("Жанр не найден.");
        }
        return genre;
    }
}
