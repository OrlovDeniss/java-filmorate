package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.storage.Storage;

@Service
public class DirectorService extends AbstractService<Director> {

    protected DirectorService(Storage<Director> storage) {
        super(storage);
    }

}