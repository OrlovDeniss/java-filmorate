package ru.yandex.practicum.filmorate.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MovieBirthdayOrLaterValidator.class)
public @interface MovieBirthdayOrLater {

    String message() default "Invalid date: must be 02/28/1895 or later.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
