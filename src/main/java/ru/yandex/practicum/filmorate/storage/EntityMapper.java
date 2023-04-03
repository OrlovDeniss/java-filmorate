package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

public interface EntityMapper<T> extends RowMapper<T> {

    String getTableName();

    List<String> getTableFields();

    Map<String, Object> toMap(T t);
}
