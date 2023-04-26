package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {

    private final FilmService service;

    public FilmController(FilmService filmService) {
        super(filmService);
        this.service = filmService;
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable @Positive long id,
                        @PathVariable @Positive long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PathVariable @Positive long id,
                           @PathVariable @Positive long userId) {
        return service.removeLike(id, userId);
    }

    @GetMapping("popular")
    public List<Film> getTopByLikes(@RequestParam(name = "count", required = false, defaultValue = "10")
                                    @Positive Long count) {
        return service.getPopular(count);
    }

    @GetMapping("/common")
    public List<Film> commonFilms(@RequestParam @Positive Long userId,
                                  @Positive Long friendId) {
        return service.getCommonFilms(userId, friendId);
    }
}