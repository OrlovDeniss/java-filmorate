package ru.yandex.practicum.filmorate.model.user;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

@Data
public class Status extends AbstractEntity {

    private String name;

}
