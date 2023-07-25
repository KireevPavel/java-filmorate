package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAllUsers();

    User addUser(User user);

    User updateUser(User user);

    User addFriend(long userId, long friendId);

    User deleteFriend(long userId, long friendId);

    List<User> getMutualFriends(long id, long otherId);

    User getUserById(long id);

    List<User> getFriendsByUserId(long id);

}
