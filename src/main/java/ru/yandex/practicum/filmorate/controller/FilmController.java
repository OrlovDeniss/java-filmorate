package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractControllerWOParams;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/films")
public class FilmController extends AbstractControllerWOParams<Film> {

    public FilmController(FilmService filmService) {
        super(filmService);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable @Positive long id,
                        @PathVariable @Positive long userId,
                        @RequestParam(name = "rate") @Positive int rate) {
        getService().addLike(id, userId, rate);
    }

    @Override
    protected FilmService getService() {
        return (FilmService) service;
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PathVariable @Positive long id,
                           @PathVariable @Positive long userId) {
        return getService().removeLike(id, userId);
    }

    @GetMapping("popular")
    public List<Film> getTopByLikes(@RequestParam(name = "count", required = false, defaultValue = "10")
                                    @Positive Long count,
                                    @RequestParam(name = "genreId", required = false)
                                    @Positive Long genreId,
                                    @RequestParam(name = "year", required = false)
                                    @Positive Long year) {
        return getService().getPopular(count, genreId, year);
    }

    @GetMapping("/common")
    public List<Film> commonFilms(@RequestParam @Positive Long userId,
                                  @Positive Long friendId) {
        return getService().getCommonFilms(userId, friendId);
    }

    @GetMapping("director/{directorId}")
    public List<Film> getFilmsByYearOrLikes(@PathVariable @Positive long directorId,
                                            @RequestParam(name = "sortBy") String sortBy) {
        return getService().getDirectorFilmsSortBy(directorId, selectSortBy(sortBy));
    }

    @GetMapping("/search")
    public List<Film> searchByDirectorOrTitle(@RequestParam(name = "query") String word,
                                              @RequestParam(name = "by", defaultValue = "title") String location) {
        return getService().searchByDirectorOrTitle(word, location);
    }

    private String selectSortBy(String sortBy) {
        switch (sortBy) {
            case "year":
                return "release";
            case "likes":
                return "rate";
            default:
                throw new IncorrectParameterException("Сортировка по параметру " +
                        sortBy, " не реализована.");
        }
    }
}
