package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.validation.MovieBirthdayOrLater;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private long id;
    @NotBlank(message = "Name must be not blank.")
    private String name;
    @Size(max = 200, message = "Description must be max 200 characters.")
    private String description;
    @MovieBirthdayOrLater(message = "Release date must be 02/28/1895 or later.")
    private LocalDate releaseDate;
    @Positive(message = "Duration must be > 0.")
    private int duration;
}
