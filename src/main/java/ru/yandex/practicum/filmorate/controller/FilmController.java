package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable @Positive Long id) {
        return filmService.findFilmById(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.findAll();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable @Positive long id,
                        @PathVariable @Positive long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PathVariable @Positive long id,
                           @PathVariable @Positive long userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping("popular")
    public List<Film> getTopByLikes(@RequestParam(name = "count", required = false, defaultValue = "10")
                                    @Positive Long count) {
        return filmService.getTopByLikes(count);
    }

}
