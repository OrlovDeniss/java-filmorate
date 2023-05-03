package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

@Data
@NoArgsConstructor
public class MPARating extends AbstractEntity {

    @JsonProperty("name")
    private String name;

}
