package ru.yandex.practicum.filmorate.storage.user.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.Review;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.user.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("ReviewDbStorage")
public class ReviewDbStorage extends AbstractDbStorage<Review> implements ReviewStorage {
    private final ReviewLikesDbStorage likesDbStorage;
    private final String sqlQuery = "with dc as" +
            " (select review_id, count(user_id) as d" +
            " from user_review_like" +
            " where is_like = false group by review_id)," +
            " l as (select review_id, count(user_id) as lc" +
            " from " + mapper.getTableName() +
            " where is_like = true group by review_id)" +
            " select id," + getFieldsSeparatedByCommas() +
            ", IfNull(l.lc, 0) - IfNull(d.dc, 0) as useful" +
            " from reviews as r left join l on l.review_id = r.id" +
            " left join d on d.review_id = r.id";

    protected ReviewDbStorage(JdbcTemplate jdbcTemplate, ReviewLikesDbStorage likesDbStorage) {
        super(jdbcTemplate, new ReviewMapper());
        this.likesDbStorage = likesDbStorage;
    }

    @Override
    public Optional<Review> findById(Long id) {
        var sql = sqlQuery + " where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Review addLikes(Long id, Long userId, boolean b) throws EntityNotFoundException {
        likesDbStorage.addLikes(id, userId, b);
        return findById(id).get();
    }

    @Override
    public Review deleteLikes(Long id, Long userId, boolean b) {
        likesDbStorage.deleteLikes(id, userId, b);
        return findById(id).get();
    }

    @Override
    public List<Review> findAllByFilmId(Optional<Long> filmId, int count) {
        String sql = sqlQuery;
        if (filmId.isPresent()) {
            sql = sql + " where film_id=" + filmId;
        }
        sql = sql + " order by useful desc limit " + count;

        return jdbcTemplate.query(sql, mapper);
    }
}
