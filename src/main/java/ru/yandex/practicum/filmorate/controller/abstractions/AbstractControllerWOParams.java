package ru.yandex.practicum.filmorate.controller.abstractions;

import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.service.Service;

import java.util.List;

public abstract class AbstractControllerWOParams<T extends Entity> extends AbstractController<T> {

    protected AbstractControllerWOParams(Service<T> service) {
        super(service);
    }

    @GetMapping
    public List<T> findAll() {
        return service.findAll();
    }
}
