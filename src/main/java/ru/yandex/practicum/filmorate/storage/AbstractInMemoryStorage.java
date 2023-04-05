package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.*;

public abstract class AbstractInMemoryStorage<T extends Entity> implements Storage<T> {

    protected final Map<Long, T> data = new HashMap<>();
    protected long idCounter = 1L;

    @Override
    public T save(T t) {
        t.setId(idCounter++);
        data.put(t.getId(), t);
        return data.get(t.getId());
    }

    @Override
    public T update(T t) {
        data.put(t.getId(), t);
        return data.get(t.getId());
    }

    @Override
    public Optional<T> findById(Long id) {
        if (data.containsKey(id)) {
            return Optional.of(data.get(id));
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }
}
