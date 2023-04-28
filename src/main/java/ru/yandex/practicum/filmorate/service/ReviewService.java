package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikesDbStorage;

import java.util.List;

@Service("reviewService")
public class ReviewService extends AbstractService<Review> {

    private final ReviewDbStorage storage;
    private final ReviewLikesDbStorage likesDbStorage;

    protected ReviewService(ReviewDbStorage storage, ReviewLikesDbStorage likesDbStorage) {
        super(storage);
        this.storage = storage;
        this.likesDbStorage = likesDbStorage;
    }


    public List<Review> findAllByFilmId(long filmId, int count) {
        return storage.findAllByFilmId(filmId, count);
    }

    public Review addLikes(long id, long userId) {
        likesDbStorage.addLikes(id, userId, true);
        return storage.findById(id).get();
    }

    public Review addDislike(long id, long userId) {
        likesDbStorage.addLikes(id, userId, false);
        return storage.findById(id).get();
    }

    public Review removeLikes(long id, long userId) {
        likesDbStorage.deleteLikes(id, userId, true);
        return storage.findById(id).get();
    }

    public Review removeDislikes(long id, long userId) {
        likesDbStorage.deleteLikes(id, userId, false);
        return storage.findById(id).get();
    }

    public void deleteReview(long id) {
        storage.deleteReview(id);
    }
}
