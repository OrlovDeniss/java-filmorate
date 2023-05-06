package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.Feed;
import ru.yandex.practicum.filmorate.model.user.enums.EventType;
import ru.yandex.practicum.filmorate.model.user.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.user.FeedDbStorage;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository("ReviewDbStorage")
public class ReviewDbStorage extends AbstractDbStorage<Review> implements ReviewStorage {
    private final FeedDbStorage feedStorage;
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

    protected ReviewDbStorage(JdbcTemplate jdbcTemplate, ReviewMapper mapper, FeedDbStorage feedStorage) {
        super(jdbcTemplate, mapper);
        this.feedStorage = feedStorage;
    }

    @Override
    public Review save(Review review) {
        super.save(review);
        feedStorage.saveUserFeed(Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(review.getUserId())
                .eventType(EventType.REVIEW)
                .operation(OperationType.ADD)
                .entityId(review.getId())
                .build());
        return review;
    }

    @Override
    public Review update(Review review) throws EntityNotFoundException {
        String sql = "UPDATE " + mapper.getTableName() +
                " SET content=?, is_positive=?" +
                " WHERE ID = " + review.getId();
        log.info(sql + " " + Arrays.toString(mapper.toMap(review).values().toArray()));

        Review r = findById(review.getId()).get();
        feedStorage.saveUserFeed(Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(r.getUserId())
                .eventType(EventType.REVIEW)
                .operation(OperationType.UPDATE)
                .entityId(r.getId())
                .build());
        if (jdbcTemplate.update(sql, review.getContent(), review.getIsPositive()) <= 0) {
            throw new EntityNotFoundException("Review with Id: " + review.getId() + " not found");
        }
        return r;
    }

    @Override
    public Optional<Review> delete(Long id) {
        Optional<Review> optT = super.delete(id);
        if (optT.isPresent()) {
            feedStorage.saveUserFeed(Feed.builder()
                    .timestamp(Instant.now().toEpochMilli())
                    .userId(optT.get().getUserId())
                    .eventType(EventType.REVIEW)
                    .operation(OperationType.REMOVE)
                    .entityId(optT.get().getId())
                    .build());
            return optT;
        }
        return Optional.empty();
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
    public List<Review> findAllByFilmId(long filmId, int count) throws EntityNotFoundException {
        String sql = sqlQuery;
        if (filmId != 0) {
            sql = sql + " where film_id=" + filmId;
        }
        sql = sql + " order by useful desc limit " + count;
        try {
            return jdbcTemplate.query(sql, mapper);
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("Error! Cannot find film with id:" + filmId);
        }
    }
}
