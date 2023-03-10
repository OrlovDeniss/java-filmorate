package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService extends AbstractService {

    protected FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        super(filmStorage, userStorage);
    }

    public List<Film> findAll() {
        var films = filmStorage.findAll();
        log.info("Список фильмов {}", films);
        return films;
    }

    public Film create(Film film) {
        filmStorage.save(film);
        log.info("Добавлен фильм: {}.", film);
        return findFilmById(film.getId());
    }

    public Film update(Film film) {
        findFilmById(film.getId());
        filmStorage.update(film);
        log.info("Обновлен фильм: {}.", film);
        return findFilmById(film.getId());
    }

    public Film addLike(Long filmId, Long userId) {
        var film = findFilmById(filmId);
        var user = findUserById(userId);
        film.getUsersIdWhoLike().add(user.getId());
        log.info("Пользователь {} добавил лайк к фильму: {}.", user, film);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        var film = findFilmById(filmId);
        var user = findUserById(userId);
        film.getUsersIdWhoLike().remove(user.getId());
        log.info("Пользователь {} удалил лайк к фильму: {}.", user, film);
        return film;
    }

    public List<Film> getTopByLikes(Long count) {
        var films = filmStorage.findTopLikes(count);
        log.info("Топ {} фильм(ов) по лайкам: {}.", count, films);
        return films;
    }
}
