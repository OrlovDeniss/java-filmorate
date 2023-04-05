package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.AbstractInMemoryStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {

    private final Comparator<Film> likePriority = (f1, f2) ->
            f1.getRate() < f2.getRate() ? 1 : -1;

    public List<Film> findTopByLikes(Long limit) {
        return data.values()
                .stream()
                .sorted(likePriority)
                .limit(limit)
                .collect(Collectors.toList());
    }
}