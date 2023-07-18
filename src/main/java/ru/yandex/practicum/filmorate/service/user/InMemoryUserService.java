package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {

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
    public List<User> getFriendsByUserId(long id) {
        return findAllUsers().stream()
                .filter(user -> user.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriends(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        Set<Long> mutualFriends = new HashSet<>(user.getFriends());
        mutualFriends.retainAll(friend.getFriends());
        return mutualFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }


    @Override
    public User addFriend(long userId, long friendId) {
        getUserById(userId).getFriends().add(friendId);
        getUserById(friendId).getFriends().add(userId);
        return getUserById(userId);
    }

    @Override
    public User deleteFriend(long userId, long friendId) {
        getUserById(userId).getFriends().remove(friendId);
        getUserById(friendId).getFriends().remove(userId);
        return getUserById(userId);
    }
}

