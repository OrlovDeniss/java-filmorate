package ru.yandex.practicum.filmorate.model.IdManager;

public class IdManager {

    private static long filmId = 1;
    private static long userId = 1;

    public static long getFilmId() {
        return filmId++;
    }
    public static long getUserId() {
        return userId++;
    }
}
