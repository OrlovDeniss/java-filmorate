package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {

    void containsOrElseThrow(long id);

    T save(T t);

    T update(T t);

    Optional<T> findById(Long id);

    List<T> findAll();

    Optional<T> delete(Long id);
}
