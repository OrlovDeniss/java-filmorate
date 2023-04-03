package ru.yandex.practicum.filmorate.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MovieBirthdayOrLaterValidator implements ConstraintValidator<MovieBirthdayOrLater, LocalDate> {

    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isAfter(MOVIE_BIRTHDAY) || date.isEqual(MOVIE_BIRTHDAY);
    }
}
