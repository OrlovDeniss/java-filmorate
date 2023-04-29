package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractControllerWOParams;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.service.Service;

@RestController
@Validated
@RequestMapping("/mpa")
public class MPAController extends AbstractControllerWOParams<MPARating> {

    public MPAController(Service<MPARating> service) {
        super(service);
    }

    @Override
    public MPARating add(MPARating mpaRating) {
        throw new MethodNotImplemented("Метод не реализован.");
    }

    @Override
    public MPARating update(MPARating mpaRating) {
        throw new MethodNotImplemented("Метод не реализован.");
    }

    @Override
    public void delete(Long id) {
        throw new MethodNotImplemented("Метод не реализован");
    }

    @Override
    protected Service<MPARating> getService() {
        return service;
    }
}
