package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable @Positive Long id) {
        return userService.findUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(@PathVariable @Positive Long id,
                          @PathVariable @Positive Long friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User removeFriend(@PathVariable @Positive Long id,
                             @PathVariable @Positive Long friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable @Positive Long id) {
        return userService.findUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable @Positive Long id,
                                       @PathVariable @Positive Long otherId) {
        return userService.findMutualFriends(id, otherId);
    }
}
