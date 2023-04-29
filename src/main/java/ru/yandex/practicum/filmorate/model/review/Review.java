package ru.yandex.practicum.filmorate.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Review extends AbstractEntity {

    @NotBlank
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Long userId;
    @NotNull
    private Long filmId;
    private Integer useful;

    @JsonProperty("reviewId")
    @Override
    public Long getId() {
        return super.getId();
    }
}
