package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

@Data
@NoArgsConstructor
public class Genre extends AbstractEntity {

    @JsonProperty("name")
    private String name;

    public Genre(long id) {
        super(id);
    }

    public Genre(Long id, String name) {
        super(id);
        this.name = name;
    }
}
