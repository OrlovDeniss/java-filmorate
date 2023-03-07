package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private long id;
    @Email(message = "Email is not valid")
    private String email;
    @Pattern(regexp = "^\\S*$")
    @NotBlank(message = "Login must be not blank")
    private String login;
    private String name;
    @PastOrPresent(message = "Birthday must be before current date.")
    private LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
