package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.manager.IdManager;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> filmsRepository = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        log.info("Список фильмов {}", filmsRepository.values());
        return new ArrayList<>(filmsRepository.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(IdManager.getFilmId());
        filmsRepository.put(film.getId(), film);
        log.info("Добавлен фильм: {}.", film);
        return filmsRepository.get(film.getId());
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        if (filmsRepository.containsKey(film.getId())) {
            filmsRepository.put(film.getId(), film);
            log.info("Обновление фильма: {}.", film);
            return new ResponseEntity<>(filmsRepository.get(film.getId()), HttpStatus.OK);
        } else {
            log.info("Несуществующий ИД при обновлении фильма: {}.", film);
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
