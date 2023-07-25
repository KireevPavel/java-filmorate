package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.Genre;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Primary
@Slf4j
@RequiredArgsConstructor
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private Film mapRowToFilm(ResultSet rs, Set<Genre> genres) throws SQLException {
        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .genres(genres != null && genres.isEmpty() ? null : genres)
                .mpa(Mpa.builder().id(rs.getLong("rating_mpa_id")).name(rs.getString("title")).build())
                .build();
    }

    private Map<Long, Set<Genre>> getAllFilmsGenres() {
        final String sql = "SELECT * FROM film_genres INNER JOIN genres ON genres.genre_id = film_genres.genre_id";
        final Map<Long, Set<Genre>> filmsGenres = new HashMap<>();
        jdbcTemplate.query(sql, rs -> {
            final Long filmId = rs.getLong("film_id");
            filmsGenres.getOrDefault(filmId, new HashSet<>()).add(Genre.builder().id(rs.getInt("genre_id"))
                    .name(rs.getString("name")).build());
        });
        return filmsGenres;
    }

    private Set<Genre> getFilmGenresById(Long id) {
        final String sql = "SELECT * FROM film_genres INNER JOIN genres ON genres.genre_id = film_genres.genre_id"
                + " WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, getNum) -> Genre.builder().id(rs.getInt("genre_id"))
                .name(rs.getString("name")).build(), id));
    }

    @Override
    public List<Film> findAllFilms() {
        final Map<Long, Set<Genre>> filmsGenres = getAllFilmsGenres();
        final String sql = "SELECT * FROM films LEFT JOIN mpa ON films.rating_mpa_id = mpa.rating_mpa_id";
        return jdbcTemplate.query(sql, (rs, numRow) -> {
            final Long filmId = rs.getLong("film_id");
            return mapRowToFilm(rs, filmsGenres.get(filmId));
        });
    }

    @Override
    public Film getFilmById(Long id) {
        final String sql = "SELECT * FROM films WHERE film_id = ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, numRow) -> mapRowToFilm(rs, getFilmGenresById(id)), id);
        return films.size() > 0 ? films.get(0) : null;
    }

    @Override
    public void addFilm(Film film) {
        final String sql = "INSERT INTO films (film_id, name, description, release_date, duration, rating_mpa_id)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId());
        final Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null) {
            final String genreSaveSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            filmGenres.forEach(x -> jdbcTemplate.update(genreSaveSql, film.getId(), x.getId()));
        }
    }

    @Override
    public void updateFilm(Film film) {
        final String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ?"
                + " WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        final String deleteGenres = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenres, film.getId());
        final Set<Genre> filmGenres = film.getGenres();
        if (filmGenres != null) {
            final String genreSaveSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            filmGenres.forEach(x -> jdbcTemplate.update(genreSaveSql, film.getId(), x.getId()));
        }
    }

    @Override
    public List<Film> getRating(long limit) {
        final String sql = "SELECT * FROM films f LEFT JOIN (SELECT film_id, COUNT(*) likes_count FROM likes"
                + " GROUP BY film_id) l ON f.film_id = l.film_id LEFT JOIN mpa ON f.rating_mpa_id = mpa.rating_mpa_id"
                + " ORDER BY l.likes_count DESC LIMIT ?";
        final Map<Long, Set<Genre>> filmsGenres = getAllFilmsGenres();
        return jdbcTemplate.query(sql, (rs, numRow) -> {
            final Long filmId = rs.getLong("film_id");
            return mapRowToFilm(rs, filmsGenres.get(filmId));
        }, limit);
    }

    @Override
    public Film like(long filmId, long userId) {
        Film film = getFilmById(filmId);
        String sql = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        return film;
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        return film;
    }
}

