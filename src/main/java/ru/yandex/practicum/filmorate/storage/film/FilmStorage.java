package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface FilmStorage extends Storage<Film> {

    List<Film> findTopByLikes(Long limit);

    List<Film> getCommonFilms(Long userId, Long friendId);
}
