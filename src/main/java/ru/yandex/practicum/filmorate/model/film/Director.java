package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Director extends AbstractEntity {

    @NotBlank
    private String name;

}