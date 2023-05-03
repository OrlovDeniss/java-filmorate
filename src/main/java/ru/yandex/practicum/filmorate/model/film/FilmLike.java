package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class FilmLike extends AbstractEntity {

    @NotNull
    private Long filmId;
    @NotNull
    private Long userId;
    private Integer rate;
    private Boolean isPositive;
}
