package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Slf4j
@Component
public abstract class AbstractService {

    protected FilmStorage filmStorage;
    protected UserStorage userStorage;

    protected AbstractService(FilmStorage filmStorage,
                              UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film findFilmById(Long id) {
        var film = filmStorage.findById(id);
        if (film.isPresent()) {
            log.info("Фмльм {}", film.get());
            return film.get();
        }
        throw new FilmNotFoundException(String.format("Фильм id=%s не найден.", id));
    }

    public User findUserById(Long id) {
        var user = userStorage.findById(id);
        if (user.isPresent()) {
            log.info("Пользователь {}", user.get());
            return user.get();
        }
        throw new UserNotFoundException(String.format("Пользователь id=%s не найден", id));
    }
}
