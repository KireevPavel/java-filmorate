package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    private void userValidation(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public User createUser(User user) {
        userValidation(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        userValidation(user);
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(Integer id) {
        User user = userStorage.getById(id);
        userValidation(user);
        return user;
    }

    public User addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        userValidation(user);
        userValidation(friend);
        userStorage.getById(userId).getFriends().add(friendId);
        userStorage.getById(friendId).getFriends().add(userId);
        return userStorage.getById(userId);
    }

    public User deleteFriend(int userId, int friendId) {
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
        return getUserById(userId);
    }

    public List<User> getFriendsByUserId(Integer id) {
        return userStorage.getAll().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) {
            List<User> mutualFriends = new ArrayList<>();
            for (Integer id :getUserById(userId).getFriends()) {
                if (getUserById(friendId).getFriends().contains(id)) {
                    mutualFriends.add(getUserById(id));
                }
            }
            return mutualFriends;
    }
}

