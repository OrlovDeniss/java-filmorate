package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserMapper implements EntityMapper<User> {

    private static final String TABLE_NAME = "usr";
    private static final List<String> TABLE_FIELDS = List.of(
            "email",
            "login",
            "name",
            "birthday");

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<String> getTableFields() {
        return TABLE_FIELDS;
    }

    @Override
    public Map<String, Object> toMap(User user) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put(TABLE_FIELDS.get(0), user.getEmail());
        params.put(TABLE_FIELDS.get(1), user.getLogin());
        params.put(TABLE_FIELDS.get(2), user.getName());
        params.put(TABLE_FIELDS.get(3), user.getBirthday());
        return params;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) {
        User user = new User();
        try {
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString(TABLE_FIELDS.get(0)));
            user.setLogin(rs.getString(TABLE_FIELDS.get(1)));
            user.setName(rs.getString(TABLE_FIELDS.get(2)));
            user.setBirthday(rs.getDate(TABLE_FIELDS.get(3)).toLocalDate());
        } catch (SQLException e) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        return user;
    }
}
