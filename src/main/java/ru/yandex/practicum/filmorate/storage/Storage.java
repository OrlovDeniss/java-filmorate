package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {

    void save(T t);

    void update(T t);

    Optional<T> findById(Long id);

    List<T> findAll();

    void deleteById(Long id);

}
