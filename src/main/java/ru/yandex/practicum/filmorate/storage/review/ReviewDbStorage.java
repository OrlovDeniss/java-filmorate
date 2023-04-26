package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewSpecialException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("ReviewDbStorage")
public class ReviewDbStorage extends AbstractDbStorage<Review> implements ReviewStorage {
    private final ReviewLikesDbStorage likesDbStorage;
    private final String sqlQuery = "with d as" +
            " (select review_id, count(user_id) as dc" +
            " from user_review_like" +
            " where is_like = false group by review_id)," +
            " l as (select review_id, count(user_id) as lc" +
            " from user_review_like" +
            " where is_like = true group by review_id)" +
            " select id," + getFieldsSeparatedByCommas() +
            ", IfNull(l.lc, 0) - IfNull(d.dc, 0) as useful" +
            " from " + mapper.getTableName() + " as r" +
            " left join l on l.review_id = r.id" +
            " left join d on d.review_id = r.id";

    protected ReviewDbStorage(JdbcTemplate jdbcTemplate, ReviewLikesDbStorage likesDbStorage) {
        super(jdbcTemplate, new ReviewMapper());
        this.likesDbStorage = likesDbStorage;
    }

    @Override
    public Review save(Review t) throws ReviewSpecialException {
        SqlRowSet userRows1 = jdbcTemplate.queryForRowSet(
                "select id from usr where id = ?", t.getUserId());
        if (!userRows1.next()) {
            log.warn("{} with Id: {} not found",
                    "User", t.getUserId());
            throw new ReviewSpecialException("User with Id: " + t.getUserId() + " not found");
        }
        SqlRowSet userRows2 = jdbcTemplate.queryForRowSet(
                "select id from film where id = ?", t.getFilmId());
        if (!userRows2.next()) {
            log.warn("{} with Id: {} not found",
                    "User", t.getFilmId());
            throw new ReviewSpecialException("User with Id: " + t.getFilmId() + " not found");
        }
        return super.save(t);
    }

    @Override
    public Review update(Review t) {
        String sql = "UPDATE " + mapper.getTableName() +
                " SET content=?, is_positive=?" +
                " WHERE ID = " + t.getId();
        log.info(sql + " " + Arrays.toString(mapper.toMap(t).values().toArray()));
        jdbcTemplate.update(sql, t.getContent(), t.getIsPositive());
        return findById(t.getId()).get();
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
    public List<Review> findAllByFilmId(Long filmId, int count) {
        String sql = sqlQuery;
        if (filmId != 0) {
            sql = sql + " where film_id=" + filmId;
        }
        sql = sql + " order by useful desc limit " + count;
        try {
            return jdbcTemplate.query(sql, mapper);
        } catch (DataIntegrityViolationException e) {
            throw new ReviewSpecialException("Error! Cannot find film with id:" + filmId);
        }
    }

    public void deleteReview(long id) {
        jdbcTemplate.update("delete from reviews where id=" + id);
        log.debug(
                "Review with Id: {}, deleted!",
                id
        );
    }
}
