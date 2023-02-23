package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.manager.IdManager;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> usersRepository = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        log.info("Список пользователей {}", usersRepository.values());
        return new ArrayList<>(usersRepository.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        loginIsNameIfNameIsNull(user);
        user.setId(IdManager.getUserId());
        usersRepository.put(user.getId(), user);
        log.info("Добавлен пользователь: {}.", user);
        return usersRepository.get(user.getId());
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        loginIsNameIfNameIsNull(user);
        if (usersRepository.containsKey(user.getId())) {
            usersRepository.put(user.getId(), user);
            log.info("Обновление пользователя: {}.", user);
            return new ResponseEntity<>(usersRepository.get(user.getId()), HttpStatus.OK);
        } else {
            log.info("Несуществующий ИД при обновлении пользователя: {}.", user);
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void loginIsNameIfNameIsNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
