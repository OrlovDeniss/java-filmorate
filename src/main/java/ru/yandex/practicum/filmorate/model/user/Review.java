package ru.yandex.practicum.filmorate.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

@Data
public class Review extends AbstractEntity {

    private String content;
    private Boolean isPositive;
    private Long userId;
    private Long filmId;
    private Integer useful;

    @JsonProperty("reviewId")
    @Override
    public Long getId(){
        return super.getId();
    }
}
