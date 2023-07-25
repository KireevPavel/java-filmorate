package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    private Mpa mapRowToMpa(ResultSet rs) throws SQLException {
        return Mpa.builder().id(rs.getLong("rating_mpa_id")).name(rs.getString("name")).build();
    }

    public List<Mpa> findAll() {
        final String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMpa(rs));
    }

    public Mpa getMpa(long id) {
        final String sql = "SELECT * FROM mpa WHERE rating_mpa_id = ?";
        final List<Mpa> mpa = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToMpa(rs), id);
        return mpa.size() > 0 ? mpa.get(0) : null;
    }
}
