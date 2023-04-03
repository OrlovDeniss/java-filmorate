package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.AbstractInMemoryStorage;

@Component
@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {
}