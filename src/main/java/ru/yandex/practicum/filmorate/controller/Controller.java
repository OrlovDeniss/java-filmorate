package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

public interface Controller<T> {

    T get(@PathVariable @Positive Long id);

    List<T> findAll(@RequestParam Map<String, String> requestParams);

    T add(@Valid @RequestBody T t);

    T update(@Valid @RequestBody T t);

}