package ru.yandex.practicum.filmorate.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.AbstractEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User extends AbstractEntity {

    @Email
    @JsonProperty("email")
    private String email;
    @Pattern(regexp = "^\\S*$")
    @NotBlank
    @JsonProperty("login")
    private String login;
    @JsonProperty("name")
    private String name;
    @PastOrPresent
    @JsonProperty("birthday")
    private LocalDate birthday;
    @JsonProperty("friends")
    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
