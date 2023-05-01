package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.abstractions.AbstractControllerWOParams;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.Feed;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
public class UserController extends AbstractControllerWOParams<User> {

    public UserController(UserService service) {
        super(service);
    }

    @PutMapping("{id}/friends/{friendId}")
    public User addFriend(@PathVariable @Positive Long id,
                          @PathVariable @Positive Long friendId) {
        return getService().addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User removeFriend(@PathVariable @Positive Long id,
                             @PathVariable @Positive Long friendId) {
        return getService().removeFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getUserFriends(@PathVariable @Positive Long id) {
        return getService().findUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable @Positive Long id,
                                       @PathVariable @Positive Long otherId) {
        return getService().findMutualFriends(id, otherId);
    }

    @GetMapping("{id}/recommendations")
    public List<Film> getFilmRecommendation(@PathVariable @Positive Long id) {
        return getService().getFilmRecommendation(id);
    }

    @GetMapping("{id}/feed")
    public List<Feed> findAllUserFeed(@PathVariable @Positive Long id) {
        return getService().findAllUserFeed(id);
    }

    @Override
    protected UserService getService() {
        return (UserService) service;
    }
}
