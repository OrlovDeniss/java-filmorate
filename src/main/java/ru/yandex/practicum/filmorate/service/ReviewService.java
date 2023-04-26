package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.user.Review;
import ru.yandex.practicum.filmorate.storage.user.db.ReviewDbStorage;

import java.util.List;
import java.util.Optional;

public class ReviewService extends AbstractService<Review> {

    private final ReviewDbStorage storage;

    protected ReviewService(ReviewDbStorage storage) {
        super(storage);
        this.storage = storage;
    }


    public List<Review> findAllByFilmId(Optional<Long> filmId, int count) {
        return storage.findAllByFilmId(filmId, count);
    }

    public Review addLikes(long id, long userId) {
        return storage.addLikes(id, userId, true);
    }

    public Review addDislike(long id, long userId) {
        return storage.addLikes(id, userId, false);
    }

    public Review removeLikes(long id, long userId) {
        return storage.deleteLikes(id, userId, true);
    }

    public Review removeDislikes(long id, long userId) {
        return storage.deleteLikes(id, userId, false);
    }
}
