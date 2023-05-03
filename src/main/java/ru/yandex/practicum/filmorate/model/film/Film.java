package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.model.validation.MovieBirthdayOrLater;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film extends AbstractEntity {

    @NotBlank
    @JsonProperty("name")
    private String name;
    @Size(max = 200)
    @JsonProperty("description")
    private String description;
    @NotNull
    @MovieBirthdayOrLater
    @JsonProperty("releaseDate")
    private LocalDate releaseDate;
    @Positive
    @JsonProperty("duration")
    private int duration;
    @JsonProperty("rate")
    private float rate;
    @JsonProperty("genres")
    private Set<Genre> genres = new HashSet<>();
    @JsonProperty("mpa")
    private MPARating mpa = new MPARating();
    @JsonProperty("directors")
    private Set<Director> directors = new HashSet<>();

}