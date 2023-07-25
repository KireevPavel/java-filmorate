package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private void userValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    @Override
    public User addUser(User user) {
        userValidation(user);
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        userValidation(user);
        return userStorage.updateUser(user);
    }

    @Override
    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public User addFriend(long userId, long friendId) {
        userStorage.addFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        userStorage.deleteFriend(userId, friendId);
        return userStorage.getUserById(userId);
    }

    @Override
    public List<User> getUserFriends(long userId) {
        return userStorage.getFriendsByUserId(userId);
    }

    @Override
    public List<User> getMutualFriends(long userId, long otherId) {
        return userStorage.getMutualFriends(userId, otherId);
    }
}

