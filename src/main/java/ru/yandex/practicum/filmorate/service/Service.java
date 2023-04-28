package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface Service<T extends Entity> {

    T findById(Long id);

    List<T> findAll();

    T create(T t);

    T update(T t);

    void delete(Long id);
}