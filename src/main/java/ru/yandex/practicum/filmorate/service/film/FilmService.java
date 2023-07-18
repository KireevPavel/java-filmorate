package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    void like(long filmId, long userId);

    void deleteLike(long userId, long filmId);

    List<Film> getTopFilms(long count);

    List<Film> findAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long id);
}
