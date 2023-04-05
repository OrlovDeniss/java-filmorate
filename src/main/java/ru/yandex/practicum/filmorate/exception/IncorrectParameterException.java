package ru.yandex.practicum.filmorate.exception;

public class IncorrectParameterException extends RuntimeException {

    private final String parameter;
    private final String message;

    public IncorrectParameterException(String parameter, String message) {
        this.parameter = parameter;
        this.message = message;
    }

    public String getParameter() {
        return parameter;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
