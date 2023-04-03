package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;

@Service
public class GenreService extends AbstractService<Genre> {

    protected GenreService(Storage<Genre> storage) {
        super(storage);
    }
}
