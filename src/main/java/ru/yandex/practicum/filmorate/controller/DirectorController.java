package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.service.Service;

@RestController
@RequestMapping("/directors")
public class DirectorController extends AbstractController<Director> {

    public DirectorController(Service<Director> service) {
        super(service);
    }

}
