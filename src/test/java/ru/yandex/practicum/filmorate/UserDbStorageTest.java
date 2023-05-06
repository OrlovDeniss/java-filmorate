package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql("/test_data.sql")
class UserDbStorageTest {

    @Autowired
    private final UserDbStorage userDbStorage;

    @Test
    void testFindById() {
        var filmOptional = userDbStorage.findById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    void testUpdate() {
        User u = new User();
        u.setId(1L);
        u.setEmail("uuu@u.ru");
        u.setLogin("newLogin");
        u.setName("u");
        u.setBirthday(LocalDate.parse("2000-08-20"));
        u.setFriends(Set.of(2L));

        assertThat(userDbStorage.update(u)).isEqualTo(u);
    }

    @Test
    void testFindAll() {
        assertThat(userDbStorage.findAll()).hasSize(2);
    }
}