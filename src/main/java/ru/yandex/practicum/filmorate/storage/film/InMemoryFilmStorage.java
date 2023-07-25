package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private int idForFilm = 0;

    private int getIdForFilm() {
        return ++idForFilm;
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addFilm(Film film) {
        film.setLikes(new HashSet<>());
        film.setId(getIdForFilm());
        films.put(film.getId(), film);
        log.info("Поступил запрос на добавление фильма. Фильм добавлен");
    }

    @Override
    public void updateFilm(Film film) {
        if (films.get(film.getId()) != null) {
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            log.info("Поступил запрос на изменения фильма. Фильм изменён.");
        } else {
            log.error("Поступил запрос на изменения фильма. Фильм не найден.");
            throw new NotFoundException("Film not found.");
        }
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new NotFoundException("Film not found.");
    }

    @Override
    public Film like(long filmId, long userId) {
        getFilmById(filmId).getLikes().add(userId);
        return getFilmById(filmId);
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        if (getFilmById(filmId).getLikes().contains(userId)) {
            getFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new NotFoundException("Пользователь не ставил оценку данному фильму.");
        }
        return getFilmById(filmId);
    }

    @Override
    public List<Film> getRating(long limit) {
        return findAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(limit).collect(Collectors.toList());
    }
}
