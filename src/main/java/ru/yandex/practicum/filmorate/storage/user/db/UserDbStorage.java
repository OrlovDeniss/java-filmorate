package ru.yandex.practicum.filmorate.storage.user.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage extends AbstractDbStorage<User> implements UserStorage {

    private final FriendsDbStorage friendsDbStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate,
                         FriendsDbStorage friendsDbStorage) {
        super(jdbcTemplate, new UserMapper());
        this.friendsDbStorage = friendsDbStorage;
    }

    @Override
    public User save(User user) {
        super.save(user);
        friendsDbStorage.saveFriends(user.getId(), user.getFriends());
        return findById(user.getId()).get();
    }

    @Override
    public User update(User user) {
        super.update(user);
        friendsDbStorage.saveFriends(user.getId(), user.getFriends());
        return findById(user.getId()).get();
    }

    @Override
    public Optional<User> findById(Long id) {
        var optionalUser = super.findById(id);
        if (optionalUser.isPresent()) {
            optionalUser.get().setFriends(friendsDbStorage.findFriendsByUserId(id));
            return optionalUser;
        }
        return optionalUser;
    }

    @Override
    public List<User> findAll() {
        var users = super.findAll();
        users.forEach(user -> user.setFriends(friendsDbStorage.findFriendsByUserId(user.getId())));
        return users;
    }
}