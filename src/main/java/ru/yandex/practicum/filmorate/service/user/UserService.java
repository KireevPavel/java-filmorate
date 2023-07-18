package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(long id);

    List<User> getFriendsByUserId(long id);

    List<User> getMutualFriends(long userId, long friendId);

    User addFriend(long userId, long friendId);

    User deleteFriend(long userId, long friendId);
}
