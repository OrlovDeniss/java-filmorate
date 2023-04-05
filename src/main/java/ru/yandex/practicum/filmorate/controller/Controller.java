package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public interface Controller<T> {

    T get(@PathVariable @Positive Long id);

    List<T> getAll();

    T add(@Valid @RequestBody T t);

    T update(@Valid @RequestBody T t);

}