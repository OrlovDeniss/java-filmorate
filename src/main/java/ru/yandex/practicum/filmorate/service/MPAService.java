package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.MPARating;
import ru.yandex.practicum.filmorate.storage.Storage;

@Service
public class MPAService extends AbstractService<MPARating> {

    protected MPAService(Storage<MPARating> storage) {
        super(storage);
    }
}
