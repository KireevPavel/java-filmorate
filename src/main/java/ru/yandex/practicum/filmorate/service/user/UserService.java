package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
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
        userStorage.getById(userId).getFriends().add(friendId);
        userStorage.getById(friendId).getFriends().add(userId);
        return userStorage.getById(userId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
        return getUserById(userId);
    }

    public List<User> getFriendsByUserId(Integer id) {
        return userStorage.getAll().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(User user1, User user2) {
        userValidation(user1);
        userValidation(user2);
        List<User> users = getAllUsers();
        Set<Integer> user1Friends = user1.getFriends();
        Set<Integer> user2Friends = user2.getFriends();
        return user1Friends
                .stream()
                .filter(user2Friends::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }
}
