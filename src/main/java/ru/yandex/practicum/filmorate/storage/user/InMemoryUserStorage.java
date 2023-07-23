package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private long idForUser = 0;

    private long getIdForUser() {
        return ++idForUser;
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        user.setFriends(new HashSet<>());
        user.setId(getIdForUser());
        users.put(user.getId(), user);
        log.info("Поступил запрос на добавление пользователя. Пользователь добавлен.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) != null) {
            user.setFriends(new HashSet<>());
            users.put(user.getId(), user);
            log.info("Поступил запрос на изменения пользователя. Пользователь изменён.");
        } else {
            log.error("Поступил запрос на изменения пользователя. Пользователь не найден.");
            throw new NotFoundException("User not found.");
        }
        return user;
    }

    @Override
    public User getUserById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new NotFoundException("User not found.");
    }
}
