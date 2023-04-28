package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    private final UserService service;

    public UserController(UserService service) {
        super(service);
        this.service = service;
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(@PathVariable @Positive Long id,
                          @PathVariable @Positive Long friendId) {
        return service.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User removeFriend(@PathVariable @Positive Long id,
                             @PathVariable @Positive Long friendId) {
        return service.removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable @Positive Long id) {
        return service.findUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable @Positive Long id,
                                       @PathVariable @Positive Long otherId) {
        return service.findMutualFriends(id, otherId);
    }
}
