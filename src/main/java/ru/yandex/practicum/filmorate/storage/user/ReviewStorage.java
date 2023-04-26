package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.Review;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage extends Storage<Review> {
    Review addLikes(Long id, Long userId, boolean b) throws EntityNotFoundException;

    Review deleteLikes(Long id, Long userId, boolean b);

    List<Review> findAllByFilmId(Optional<Long> filmId, int count);
}
