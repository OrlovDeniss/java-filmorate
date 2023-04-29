package ru.yandex.practicum.filmorate.controller.abstractions;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

public interface Controller<T> {

    T get(@PathVariable @Positive Long id);

    T add(@Valid @RequestBody T t);

    T update(@Valid @RequestBody T t);

    void delete(@PathVariable @Positive Long id);

}