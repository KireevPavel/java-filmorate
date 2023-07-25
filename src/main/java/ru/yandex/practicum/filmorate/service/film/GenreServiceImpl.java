package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreDbStorage;

    @Override
    public Genre getGenre(long genreId) {
        return genreDbStorage.getGenreForId(genreId);
    }

    @Override
    public List<Genre> findAll() {
        return genreDbStorage.findAll();
    }

}


