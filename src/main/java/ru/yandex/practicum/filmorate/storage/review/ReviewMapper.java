package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("reviewMapper")
public class ReviewMapper implements EntityMapper<Review> {
    private static final String TABLE_NAME = "reviews";
    private static final List<String> TABLE_FIELDS = List.of(
            "content",
            "is_positive",
            "user_id",
            "film_id");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(Review review) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), review.getContent());
        params.put(TABLE_FIELDS.get(1), review.getIsPositive());
        params.put(TABLE_FIELDS.get(2), review.getUserId());
        params.put(TABLE_FIELDS.get(3), review.getFilmId());
        return params;
    }

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = Review.builder()
                .content(rs.getString(TABLE_FIELDS.get(0)))
                .isPositive(rs.getBoolean(TABLE_FIELDS.get(1)))
                .userId(rs.getLong(TABLE_FIELDS.get(2)))
                .filmId(rs.getLong(TABLE_FIELDS.get(3)))
                .useful(rs.getInt("useful"))
                .build();
        review.setId(rs.getLong("id"));
        return review;
    }
}
