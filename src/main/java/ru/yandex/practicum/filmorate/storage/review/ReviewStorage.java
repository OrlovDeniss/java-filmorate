package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface ReviewStorage extends Storage<Review> {
    Review addLikes(Long id, Long userId, boolean b) throws EntityNotFoundException;

    Review deleteLikes(Long id, Long userId, boolean b);

    List<Review> findAllByFilmId(Long filmId, int count);
}
