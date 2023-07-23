package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InMemoryFilmService implements FilmService {

    private final FilmStorage filmStorage;
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);

    private void filmValidation(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            throw new ValidationException("Некорректно указана дата релиза.");
        }
    }

    @Override
    public void like(long filmId, long userId) {
        filmStorage.getFilmById(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        } else throw new NotFoundException("Пользователь не ставил лайк этому фильму.");
    }

    @Override
    public List<Film> getTopFilms(long count) {
        return filmStorage.findAllFilms().stream().sorted((film1, film2) ->
                        film2.getLikes().size() - film1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        filmValidation(film);
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidation(film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }
}

