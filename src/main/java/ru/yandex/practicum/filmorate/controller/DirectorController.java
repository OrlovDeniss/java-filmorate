package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractControllerWOParams;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.service.Service;

@RestController
@Validated
@RequestMapping("/directors")
public class DirectorController extends AbstractControllerWOParams<Director> {

    public DirectorController(Service<Director> service) {
        super(service);
    }

    @Override
    protected Service<Director> getService() {
        return service;
    }

}