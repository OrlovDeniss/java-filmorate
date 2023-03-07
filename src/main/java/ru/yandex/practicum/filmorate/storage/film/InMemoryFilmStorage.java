package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Comparator<Film> likePriority = (f1, f2) ->
            f1.getUsersIdWhoLike().size() < f2.getUsersIdWhoLike().size() ? 1 : -1;
    private final Map<Long, Film> data = new HashMap<>();
    private long idCounter = 1L;

    @Override
    public void save(Film film) {
        film.setId(idCounter++);
        data.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        data.put(film.getId(), film);
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(Long id) {
        data.remove(id);
    }

    public List<Film> findTopLikes(Long limit) {
        return data.values()
                .stream()
                .sorted(likePriority)
                .limit(limit)
                .collect(Collectors.toList());
    }
}
