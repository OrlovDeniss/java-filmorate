package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Service
@Qualifier("filmService")
public class FilmService extends AbstractService<Film> {

    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        super(filmStorage);
        this.filmStorage = filmStorage;
    }

    public void addLike(Long filmId, Long userId) {
        var film = findById(filmId);
        film.setRate(film.getRate() + 1);
        film.addLike(userId);
        update(film);
        log.info("Пользователь id={} добавил лайк к фильму: id={}.", userId, filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        var film = findById(filmId);
        film.setRate(film.getRate() - 1);
        film.removeLike(userId);
        update(film);
        log.info("Пользователь id={} удалил лайк к фильму: id={}.", userId, filmId);
        return film;
    }

    public List<Film> getPopular(Long count) {
        var films = filmStorage.findTopByLikes(count);
        log.info("Топ {} фильм(ов) по лайкам: {}.", count, films.stream().map(Entity::getId).toArray());
        return films;
    }
}
