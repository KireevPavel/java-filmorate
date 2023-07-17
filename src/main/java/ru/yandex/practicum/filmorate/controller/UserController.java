package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Поступил запрос на создание пользователя.");
        return userService.createUser(user);
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Поступил запрос на обновление пользователя.");
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Поступил запрос на добавления в друзья.");
       return userService.addFriend(id, friendId);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Поступил запрос на получение списка пользователей.");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Поступил запрос на получение пользователя по id.");
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Поступил запрос на получение списка друзей.");
        return userService.getFriendsByUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable User user1, @PathVariable User user2) {
        log.info("Поступил запрос на получения списка общих друзей.");
        return userService.getMutualFriends(user1, user2);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Поступил запрос на удаление из друзей.");
        userService.deleteFriend(id, friendId);
    }
}


