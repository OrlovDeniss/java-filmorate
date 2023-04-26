package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService service;

    protected ReviewController(ReviewService service) {
        this.service = service;
    }

    @GetMapping("{id}")
    public Review get(@PathVariable @Positive Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Review add(@Valid @RequestBody Review t) {
        return service.create(t);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review t) {
        return service.update(t);
    }

    @GetMapping
    @ResponseBody
    public List<Review> getAll(
            @RequestParam(defaultValue = "0") Long filmId,
            @RequestParam(defaultValue = "10") Integer count) {
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

    @DeleteMapping("{id}")
    public void deleteReview(@PathVariable @Positive long id) {

        service.deleteReview(id);
    }
}
