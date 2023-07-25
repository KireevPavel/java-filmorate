package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeDbStorage likeDbStorage;

    @Override
    public Set<Long> getLikesForCurrentFilm(long id) {
        return likeDbStorage.getLikesForCurrentFilm(id);
    }
}