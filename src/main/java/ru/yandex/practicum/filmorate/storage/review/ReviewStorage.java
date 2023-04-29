package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface ReviewStorage extends Storage<Review> {
    List<Review> findAllByFilmId(long filmId, int count);
}
