package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractDbStorage<T extends Entity> implements Storage<T> {

    protected final JdbcTemplate jdbcTemplate;
    protected final EntityMapper<T> mapper;

    protected AbstractDbStorage(JdbcTemplate jdbcTemplate,
                                EntityMapper<T> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public void containsOrElseThrow(long id) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet(
                "select id from " + mapper.getTableName() + " where id = ?", id);
        if (!rows.next()) {
            log.warn("{} with Id: {} not found",
                    mapper.getTableName(), id);
            throw new EntityNotFoundException(mapper.getTableName() + " with Id: " + id + " not found");
        }
    }

    @Override
    public T save(T t) {
        t.setId(new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(mapper.getTableName())
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(mapper.toMap(t)).longValue());
        log.info("Сохранен: {}", t);
        return t;
    }

    @Override
    public T update(T t) {
        String sql = "UPDATE " + mapper.getTableName() +
                " SET " + getFieldsWithQuestionMark() +
                " WHERE ID = " + t.getId();
        log.info(sql + " " + Arrays.toString(mapper.toMap(t).values().toArray()));
        jdbcTemplate.update(sql, mapper.toMap(t).values().toArray());
        return t;
    }

    @Override
    public Optional<T> findById(Long id) {
        String sql = "SELECT ID," + getFieldsSeparatedByCommas() +
                " FROM " + mapper.getTableName() +
                " WHERE ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        return jdbcTemplate.query("SELECT ID, " +
                getFieldsSeparatedByCommas() +
                " FROM " + mapper.getTableName(), mapper);
    }

    @Override
    public Optional<T> delete(Long id) {
        Optional<T> optT = findById(id);
        jdbcTemplate.update("DELETE FROM " + mapper.getTableName() + " WHERE id = ?", id);
        return optT;
    }

    protected String getFieldsWithQuestionMark() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String field : mapper.getTableFields()) {
            stringBuilder.append(field).append(" = ?, ");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        return stringBuilder.toString();
    }

    protected String getFieldsSeparatedByCommas() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String field : mapper.getTableFields()) {
            stringBuilder.append(field).append(", ");
        }
        if (stringBuilder.length() > 2) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        return stringBuilder.toString();
    }
}