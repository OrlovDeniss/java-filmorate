package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
class UserFriendsExtractor implements ResultSetExtractor<Map<Long, Set<Long>>> {

    @Override
    public Map<Long, Set<Long>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Set<Long>> usersFriends = new LinkedHashMap<>();
        while (rs.next()) {
            Long userId = rs.getLong("user_id");
            usersFriends.putIfAbsent(userId, new HashSet<>());
            Long filmId = rs.getLong("user_friend_id");
            usersFriends.get(userId).add(filmId);
        }
        return usersFriends;
    }

}
