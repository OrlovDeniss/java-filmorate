package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql("/test_data.sql")
class FilmDbStorageTest {

    @Autowired
    private final FilmDbStorage filmDbStorage;

    @Test
    void testFindById() {
        var filmOptional = filmDbStorage.findById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void testUpdate() {
        var f2 = new Film();
        f2.setId(1L);
        f2.setName("newName");
        f2.setDescription("newDesc");
        f2.setDuration(300);
        f2.setReleaseDate(LocalDate.of(2020, 12, 1));
        f2.setGenres(Set.of(new Genre(3L, "Мультфильм")));
        f2.setLikes(Set.of(1L, 2L));

        assertThat(filmDbStorage.update(f2)).isEqualTo(f2);
    }

    @Test
    void testFindAll() {
        assertThat(filmDbStorage.findAll()).hasSize(2);
    }

    @Test
    void findTopByLikes() {
        assertThat(filmDbStorage.findTopByLikes(1L).get(0).getId()).isEqualTo(1);
    }
}