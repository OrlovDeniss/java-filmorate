package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/reviews")
public class ReviewController extends AbstractController<Review> {


    protected ReviewController(ReviewService service) {
        super(service);
    }

    @GetMapping
    public List<Review> findAllByFilmId(
            @RequestParam(defaultValue = "0") Long filmId,
            @RequestParam(defaultValue = "10") Integer count) {
        return getService().findAllByFilmId(filmId, count);
    }

    @PutMapping("{id}/like/{userId}")
    public Review addLike(@PathVariable @Positive long id,
                          @PathVariable @Positive long userId) {
        return getService().addLikes(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Review removeLike(@PathVariable @Positive long id,
                             @PathVariable @Positive long userId) {
        return getService().removeLikes(id, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Review addDislike(@PathVariable @Positive long id,
                             @PathVariable @Positive long userId) {
        return getService().addDislike(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Review removeDislike(@PathVariable @Positive long id,
                                @PathVariable @Positive long userId) {
        return getService().removeDislikes(id, userId);
    }

    @DeleteMapping("{id}")
    public void deleteReview(@PathVariable @Positive long id) {

        getService().deleteReview(id);
    }

    @Override
    public List<Review> findAll() {
        return null;
    }

    @Override
    public ReviewService getService() {
        return (ReviewService) service;
    }
}
