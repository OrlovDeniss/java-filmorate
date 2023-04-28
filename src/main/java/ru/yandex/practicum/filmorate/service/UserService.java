package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("userService")
public class UserService extends AbstractService<User> {

    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        super(storage);
    }

    @Override
    public User create(User user) {
        nameIsLoginIfNameIsNull(user);
        return super.create(user);
    }

    @Override
    public User update(User user) {
        nameIsLoginIfNameIsNull(user);
        return super.update(user);
    }

    public User addFriend(Long id, Long friendId) {
        if (Objects.equals(id, friendId)) {
            throw new IncorrectParameterException(
                    "id, friendId", "параметры должны быть !=");
        }
        var user = findById(id);
        user.addFriend(friendId);
        log.info("Пользователь {} добавил друга {}", id, friendId);
        super.update(user);
        return user;
    }

    public User removeFriend(Long id, Long friendId) {
        var user = findById(id);
        var friend = findById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(id);
        log.info("Пользователь {} удалил друга {}", id, friendId);
        super.update(user);
        super.update(friend);
        return user;
    }

    public List<User> findUserFriends(Long id) {
        var user = findById(id);
        var friendsId = user.getFriends();
        return friendsId
                .stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(Long id, Long otherId) {
        var user = findById(id);
        var otherUser = findById(otherId);
        var userFriendsId = user.getFriends();
        var otherUserFriendsId = otherUser.getFriends();
        return userFriendsId
                .stream()
                .filter(otherUserFriendsId::contains)
                .map(this::findById)
                .collect(Collectors.toList());
    }

    private void nameIsLoginIfNameIsNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void deleteUser(Long id) {
        super.delete(id);
    }
}
