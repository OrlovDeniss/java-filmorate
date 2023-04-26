package ru.yandex.practicum.filmorate.storage.user.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;

@Slf4j
@Component
public class ReviewLikesDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public ReviewLikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected void addLikes(Long id, Long userId, boolean b) throws EntityNotFoundException {
        String isLike = b ? "like" : "dislike";
        try {
            jdbcTemplate.update("INSERT INTO user_review_like" +
                    "  (review_id, user_id, is_like)" +
                    "VALUES" +
                    "  (?, ?, ?)" +
                    "ON DUPLICATE KEY UPDATE" +
                    "  is_like = VALUES(is_like)", id, userId, b);
            log.debug(
                    "Review with Id: {} get {} from user with Id: {}.",
                    id, isLike, userId
            );
        } catch (DataIntegrityViolationException e) {
            log.warn(
                    "Error! Cannot add user Id: {} {}, user not found.",
                    userId, isLike
            );
            throw new EntityNotFoundException("Error! Cannot add user Id: "
                    + userId + " " + isLike + ", user not found.");
        }
    }

    protected void deleteLikes(Long id, Long userId, boolean b) {
        jdbcTemplate.update("delete from user_review_like where review_id=? " +
                "and user_id=?", id, userId);
        log.debug(
                "Review with Id: {} get {} from user with Id: {}.",
                id, b ? "like" : "dislike", userId
        );
    }
}

