package ru.yandex.practicum.filmorate.storage.film.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DirectorMapper implements EntityMapper<Director> {

    @Override
    public String getTableName() {
        return "DIRECTOR";
    }

    @Override
    public List<String> getTableFields() {
        return List.of("name");
    }

    @Override
    public Map<String, Object> toMap(Director director) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(getTableFields().get(0), director.getName());
        return params;
    }

    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director();
        try {
            director.setId(rs.getLong("id"));
            director.setName(rs.getString(getTableFields().get(0)));
        } catch (SQLException e) {
            throw new EntityNotFoundException("Режиссер не найден.");
        }
        return director;
    }
}
