package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;

@Service("reviewService")
@Slf4j
public class ReviewService extends AbstractService<Review> {

    private final ReviewDbStorage storage;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final ReviewLikesDbStorage likesDbStorage;


    protected ReviewService(ReviewDbStorage storage, UserDbStorage userStorage,
                            FilmDbStorage filmStorage, ReviewLikesDbStorage likesDbStorage) {
        super(storage);
        this.storage = storage;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.likesDbStorage = likesDbStorage;
    }

    @Override
    public Review create(Review t) {
        userStorage.containsOrElseThrow(t.getUserId());
        filmStorage.containsOrElseThrow(t.getFilmId());
        storage.save(t);
        log.info("Создан: {}.", t);
        return findById(t.getId());
    }

    public List<Review> findAllByFilmId(long filmId, int count) throws EntityNotFoundException {
        return storage.findAllByFilmId(filmId, count);
    }

    public Review addLikes(long id, long userId) throws EntityNotFoundException {
        userStorage.containsOrElseThrow(userId);
        likesDbStorage.addLikes(id, userId, true);
        return storage.findById(id).get();
    }

    public Review removeLikes(long id, long userId) throws EntityNotFoundException {
        likesDbStorage.deleteLikes(id, userId, true);
        return storage.findById(id).get();
    }

    public Review addDislike(long id, long userId) throws EntityNotFoundException {
        userStorage.containsOrElseThrow(userId);
        likesDbStorage.addLikes(id, userId, false);
        return storage.findById(id).get();
    }

    public Review removeDislikes(long id, long userId) throws EntityNotFoundException {
        likesDbStorage.deleteLikes(id, userId, false);
        return storage.findById(id).get();
    }
}
