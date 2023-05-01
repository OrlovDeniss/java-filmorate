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

        filmStorage.addLike(filmId, userId);
    }

    public Film removeLike(Long filmId, Long userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(Long count) {
        var films = filmStorage.findTopByLikes(count);
        log.info("Топ {} фильм(ов) по лайкам: {}.", count, films.stream().map(Entity::getId).toArray());
        return films;
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        var films = filmStorage.getCommonFilms(userId, friendId);
        log.info("Получение общих фильмов для user с id {} и друга с id {}", userId, friendId);
        return films;
    }

    public List<Film> getDirectorFilmsSortBy(Long directorId, String sortBy) {
        var films = filmStorage.getDirectorFilmsSortBy(directorId, sortBy);
        log.info("Топ режисера {} по {}: {}", directorId, sortBy, films.stream().map(Entity::getId).toArray());
        return films;
    }
}