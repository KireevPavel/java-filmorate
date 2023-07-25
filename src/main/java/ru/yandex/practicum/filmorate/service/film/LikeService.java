package ru.yandex.practicum.filmorate.service.film;

import java.util.Set;

public interface LikeService {

    Set<Long> getLikesForCurrentFilm(long id);
}
