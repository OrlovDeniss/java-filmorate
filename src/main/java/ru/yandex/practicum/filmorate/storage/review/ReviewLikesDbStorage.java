package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReviewLikesDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public ReviewLikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLikes(Long id, Long userId, boolean isLike) {
        if (jdbcTemplate.update("update user_review_like " +
                        "set is_like = ? where review_id = ? and user_id = ?",
                isLike, id, userId) <= 0) {
            jdbcTemplate.update("INSERT INTO user_review_like" +
                    "(review_id, user_id, is_like) " +
                    "VALUES(?, ?, ?)", id, userId, isLike);
        }
        log.debug(
                "Review with Id: {} get {} from user with Id: {}.",
                id, isLike ? "like" : "dislike", userId
        );
    }

    public void deleteLikes(Long id, Long userId, boolean isLike) {
        jdbcTemplate.update("delete from user_review_like where review_id=? " +
                "and user_id=?", id, userId);
        log.debug(
                "Review with Id: {}, {} from user with Id: {}, deleted!",
                id, isLike ? "like" : "dislike", userId
        );
    }
}

