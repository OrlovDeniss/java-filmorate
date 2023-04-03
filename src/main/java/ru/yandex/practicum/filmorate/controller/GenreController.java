package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.Service;

@RestController
@Validated
@RequestMapping("/genres")
public class GenreController extends AbstractController<Genre> {

    public GenreController(Service<Genre> service) {
        super(service);
    }

    @Override
    public Genre add(Genre genre) {
        throw new MethodNotImplemented("Метод не реализован.");
    }

    @Override
    public Genre update(Genre genre) {
        throw new MethodNotImplemented("Метод не реализован.");
    }
}
