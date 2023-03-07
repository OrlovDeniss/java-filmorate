package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractService {

    public UserService(FilmStorage filmStorage, UserStorage userStorage) {
        super(filmStorage, userStorage);
    }

    public List<User> findAll() {
        var users = userStorage.findAll();
        log.info("Список пользователей {}", users);
        return users;
    }

    public User create(User user) {
        loginIsNameIfNameIsNull(user);
        userStorage.save(user);
        log.info("Добавлен пользователь: {}.", user);
        return findUserById(user.getId());
    }

    public User update(User user) {
        loginIsNameIfNameIsNull(user);
        findUserById(user.getId());
        userStorage.update(user);
        log.info("Обновление пользователя: {}.", user);
        return user;
    }

    public User addFriend(Long id, Long friendId) {
        if (Objects.equals(id, friendId)) {
            throw new IncorrectParameterException(
                    "id, friendId", "параметры должны быть !=");
        }
        var user = findUserById(id);
        user.addFriend(friendId);
        var friend = findUserById(friendId);
        friend.addFriend(id);
        log.info("Пользователь {} добавил друга {}", user, friend);
        return user;
    }

    public User removeFriend(Long id, Long friendId) {
        var user = findUserById(id);
        var friend = findUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(id);
        log.info("Пользователь {} удалил друга {}", user, friend);
        return user;
    }

    public List<User> findUserFriends(Long id) {
        var user = findUserById(id);
        var friendsId = user.getFriends();
        return friendsId
                .stream()
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(Long id, Long otherId) {
        var user = findUserById(id);
        var otherUser = findUserById(otherId);
        var userFriendsId = user.getFriends();
        var otherUserFriendsId = otherUser.getFriends();
        return userFriendsId
                .stream()
                .filter(otherUserFriendsId::contains)
                .map(this::findUserById)
                .collect(Collectors.toList());
    }

    private void loginIsNameIfNameIsNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
