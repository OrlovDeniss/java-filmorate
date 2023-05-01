package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.Feed;
import ru.yandex.practicum.filmorate.model.user.enums.EventType;
import ru.yandex.practicum.filmorate.model.user.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeedMapper implements EntityMapper<Feed> {

    private static final String TABLE_NAME = "feed";
    private static final List<String> TABLE_FIELDS = List.of(
            "feed_timestamp",
            "user_id",
            "event_type",
            "operation",
            "entity_id");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(Feed feed) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), feed.getTimestamp());
        params.put(TABLE_FIELDS.get(1), feed.getUserId());
        params.put(TABLE_FIELDS.get(2), feed.getEventType().getEventTypeName());
        params.put(TABLE_FIELDS.get(3), feed.getOperation().getOperationName());
        params.put(TABLE_FIELDS.get(4), feed.getEntityId());
        return params;
    }

    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = Feed.builder()
                .timestamp(rs.getLong(TABLE_FIELDS.get(0)))
                .userId(rs.getLong(TABLE_FIELDS.get(1)))
                .eventType(EventType.fromString(rs.getString(TABLE_FIELDS.get(2))))
                .operation(OperationType.fromString(rs.getString(TABLE_FIELDS.get(3))))
                .entityId(rs.getLong(TABLE_FIELDS.get(4)))
                .build();
        feed.setId(rs.getLong("id"));
        return feed;
    }
}
