package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MovieBirthdayOrLaterValidator implements ConstraintValidator<MovieBirthdayOrLater, LocalDate> {

    private final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isAfter(MOVIE_BIRTHDAY) || date.isEqual(MOVIE_BIRTHDAY);
    }
}