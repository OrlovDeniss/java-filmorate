package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {

    private final static String defaultMessage = "Пользователь не найден";

    public UserNotFoundException() {
        super(defaultMessage);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
