package ru.yandex.practicum.filmorate.storage.review;

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

    public void addLikes(Long id, Long userId, boolean b) throws EntityNotFoundException {
        String isLike = b ? "like" : "dislike";
        try {
            if (jdbcTemplate.update("update user_review_like " +
                    "set is_like = ? where review_id = ? and user_id = ?",
                    b, id, userId) <= 0) {
                jdbcTemplate.update("INSERT INTO user_review_like" +
                        "(review_id, user_id, is_like) " +
                        "VALUES(?, ?, ?)", id, userId, b);
            }
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

    public void deleteLikes(Long id, Long userId, boolean b) {
        jdbcTemplate.update("delete from user_review_like where review_id=? " +
                "and user_id=?", id, userId);
        log.debug(
                "Review with Id: {}, {} from user with Id: {}, deleted!",
                id, b ? "like" : "dislike", userId
        );
    }
}

