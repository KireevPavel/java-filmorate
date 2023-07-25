package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAllFilms();

    void addFilm(Film film);

    void updateFilm(Film film);

    Film getFilmById(Long id);

    Film like(long filmId, long userId);

    Film deleteLike(long filmId, long userId);

    List<Film> getRating(long limit);
}
