package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.user.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/reviews")
public class ReviewController extends AbstractController<Review> {

    private final ReviewService service;

    protected ReviewController(ReviewService service) {
        super(service);
        this.service = service;
    }

    @Override
    public List<Review> getAll() {
        throw new MethodNotImplemented("Метод не реализован.");
    }

    @GetMapping("?filmId={filmId}&count={count}")
    public List<Review> findAllByFilmId(
            @RequestParam(value = "filmId") Optional<Long> filmId,
            @RequestParam(value = "count", defaultValue = "10") int count) {
        return service.findAllByFilmId(filmId, count);
    }

    @PutMapping("{id}/like/{userId}")
    public Review addLike(@PathVariable @Positive long id,
                          @PathVariable @Positive long userId) {
        return service.addLikes(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Review removeLike(@PathVariable @Positive long id,
                             @PathVariable @Positive long userId) {
        return service.removeLikes(id, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Review addDislike(@PathVariable @Positive long id,
                             @PathVariable @Positive long userId) {
        return service.addDislike(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Review removeDislike(@PathVariable @Positive long id,
                                @PathVariable @Positive long userId) {
        return service.removeDislikes(id, userId);
    }
}
