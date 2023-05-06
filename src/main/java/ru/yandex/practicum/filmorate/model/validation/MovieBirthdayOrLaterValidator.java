package ru.yandex.practicum.filmorate.model.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class MovieBirthdayOrLaterValidator implements ConstraintValidator<MovieBirthdayOrLater, LocalDate> {

    private static final LocalDate MOVIE_BIRTHDAY = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date != null) {
            return date.isAfter(MOVIE_BIRTHDAY) || date.isEqual(MOVIE_BIRTHDAY);
        } else {
            return false;
        }
    }
}