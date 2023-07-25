package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);

    private void filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            throw new ValidationException("Некорректно указана дата релиза.");
        }
    }

    @Override
    public Film like(long filmId, long userId) {
        return filmStorage.like(filmId, userId);
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getRating(long limit) {
        return filmStorage.getRating(limit);
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @Override
    public void addFilm(Film film) {
        filmValidation(film);
        filmStorage.addFilm(film);
    }

    @Override
    public void updateFilm(Film film) {
        filmValidation(film);
        filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }
}

