package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendsDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserFriendsExtractor userFriendsExtractor;

    protected void saveFriends(Long userId, Set<Long> friends) {
        deleteAllFriends(userId);
        jdbcTemplate.batchUpdate("INSERT INTO USER_FRIEND (USER_ID, USER_FRIEND_ID) VALUES (?, ?)",
                new ArrayList<>(friends),
                100,
                (PreparedStatement ps, Long friend) -> {
                    ps.setLong(1, userId);
                    ps.setLong(2, friend);
                });
        updateFriendStatus();
    }

    protected void updateFriendStatus() {
        jdbcTemplate.execute("UPDATE USER_FRIEND SET STATUS_ID = 1 " +
                "WHERE (USER_ID, USER_FRIEND_ID)" +
                "        IN (SELECT USER_FRIEND_ID, USER_ID FROM USER_FRIEND);" +
                "UPDATE USER_FRIEND SET STATUS_ID = 2 " +
                "WHERE (USER_ID, USER_FRIEND_ID)" +
                "          NOT IN (SELECT USER_FRIEND_ID, USER_ID FROM USER_FRIEND);");
    }

    protected Set<Long> findFriendsByUserId(Long id) {
        String sql = "SELECT USER_FRIEND_ID FROM USER_FRIEND WHERE USER_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getLong(1), id));
    }

    protected Map<Long, Set<Long>> findUserFriends() {
        return jdbcTemplate.query("SELECT USER_ID, USER_FRIEND_ID FROM USER_FRIEND", userFriendsExtractor);
    }

    protected void deleteAllFriends(Long id) {
        jdbcTemplate.update("DELETE FROM USER_FRIEND WHERE USER_ID=?", id);
    }

}
