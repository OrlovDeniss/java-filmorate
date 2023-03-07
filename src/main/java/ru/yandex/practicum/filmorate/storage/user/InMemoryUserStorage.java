package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> data = new HashMap<>();
    private long idCounter = 1L;

    @Override
    public void save(User user) {
        user.setId(idCounter++);
        data.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(Long id) {
        data.remove(id);
    }
}
