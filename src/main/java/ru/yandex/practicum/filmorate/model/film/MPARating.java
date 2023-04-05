package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

@Data
@NoArgsConstructor
public class MPARating extends AbstractEntity {

    private String name;

}
