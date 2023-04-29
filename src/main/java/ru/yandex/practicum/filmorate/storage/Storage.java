package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {

    T save(T t);

    T update(T t);

    Optional<T> findById(Long id);

    List<T> findAll();

    void delete(Long id);

    boolean existsById(Long id);

    void existsByIdOrThrow(Long id);
}
