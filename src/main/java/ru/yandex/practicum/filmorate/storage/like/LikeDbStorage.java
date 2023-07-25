package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final JdbcTemplate jdbcTemplate;

    public Set<Long> getLikesForCurrentFilm(long id) {
        Set<Long> likes = new HashSet<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT like_id, film_id, user_id FROM likes");
        while (likeRows.next()) {
            if (likeRows.getInt("film_id") == id) {
                likes.add(likeRows.getLong("like_id"));
            }
        }
        return likes;
    }
}
