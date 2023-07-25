package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    private Genre mapRowToGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public List<Genre> findAll() {
        final String sql = "SELECT genre_id, name FROM genre_type";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Genre getGenreForId(long id) {
        final String sql = "SELECT genre_id, name FROM genre_type WHERE genre_id=?";
        final List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs), id);
        return genres.size() > 0 ? genres.get(0) : null;
        }

}
