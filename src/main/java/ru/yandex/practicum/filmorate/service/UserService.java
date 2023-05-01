package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.Feed;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.model.user.enums.EventType;
import ru.yandex.practicum.filmorate.model.user.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("userService")
public class UserService extends AbstractService<User> {
    private final FeedDbStorage feedStorage;
    private final FilmDbStorage filmStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage storage,
                       FilmDbStorage filmStorage,
                       FeedDbStorage feedStorage) {
        super(storage);
        this.filmStorage = filmStorage;
        this.feedStorage = feedStorage;
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
        feedStorage.saveUserFeed(Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(id)
                .eventType(EventType.FRIEND)
                .operation(OperationType.ADD)
                .entityId(friendId)
                .build());
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
        feedStorage.saveUserFeed(Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(id)
                .eventType(EventType.FRIEND)
                .operation(OperationType.REMOVE)
                .entityId(friendId)
                .build());
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

    public List<Film> getFilmRecommendation(Long id) {
        return filmStorage.getFilmRecommendation(id);
    }

    public List<Feed> findAllUserFeed(Long id) {
        storage.containsOrElseThrow(id);
        return feedStorage.findAllUserFeed(id);
    }

    private void nameIsLoginIfNameIsNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
