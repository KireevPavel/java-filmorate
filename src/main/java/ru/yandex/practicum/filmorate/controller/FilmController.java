package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/films", produces = "application/json")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Поступил запрос на добавление фильма.");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.info("Поступил запрос на изменения фильма.");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{filmId}")
    public void like(@PathVariable long id, @PathVariable long filmId) {
        log.info("Поступил запрос на присвоение лайка фильму.");
        filmService.like((int)id, (int)filmId);
    }

    @GetMapping()
    public List<Film> getFilms() {
        log.info("Поступил запрос на получение списка всех фильмов.");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        log.info("Получен GET-запрос на получение фильма");
        return filmService.getFilmById((int)id);
    }

    @GetMapping("/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10") long count) {
        log.info("Поступил запрос на получение списка популярных фильмов.");
        return filmService.getTopFilms((int)count);
    }

    @DeleteMapping("/{id}/like/{filmId}")
    public void deleteLike(@PathVariable long id, @PathVariable long filmId) {
        log.info("Поступил запрос на удаление лайка у фильма.");
        filmService.deleteLike((int)filmId,(int) id);
    }
}
