package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface FilmStorage extends Storage<Film> {

    List<Film> findTopByLikes(Long limit, Long genreId, Long year);

    Film addLike(long k1, long k2) throws EntityNotFoundException;

    Film deleteLike(long k1, long k2) throws EntityNotFoundException;

    List<Film> getCommonFilms(Long userId, Long friendId);

    List<Film> getFilmRecommendation(Long id);

    List<Film> getDirectorFilmsSortBy(Long directorId, String sortBy);

    List<Film> searchByDirectorOrTitle(String word, String[] locationsForSearch);
}