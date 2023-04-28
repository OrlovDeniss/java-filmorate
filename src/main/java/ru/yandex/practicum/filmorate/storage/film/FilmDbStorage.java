package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.EntityMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private final GenreDbStorage genreDbStorage;
    private final MPADbStorage mpaDbStorage;
    private final String sqlQuery = "with l as" +
            " (select film_id, count(user_id) as lc" +
            " from user_film_like" +
            " group by film_id)" +
            " select id, " +
            getFieldsSeparatedByCommas() +
            ", l.lc as rate" +
            " from " + mapper.getTableName() + " as f" +
            " left join l on l.film_id = f.id";

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         EntityMapper<Film> mapper,
                         GenreDbStorage genreDbStorage,
                         MPADbStorage mpaDbStorage) {
        super(jdbcTemplate, mapper);
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Film save(Film film) {
        saveFilmProperties(super.save(film));
        return findById(film.getId()).get();
    }

    @Override
    public Film update(Film film) {
        saveFilmProperties(super.update(film));
        return findById(film.getId()).get();
    }

    @Override
    public Optional<Film> findById(Long id) {
        Optional<Film> film;
        var sql = sqlQuery + " where f.id = ?";
        try {
            film = Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            film = Optional.empty();
        }

        if (film.isPresent()) {
            film.get().setGenres(genreDbStorage.findFilmGenres(id));
            log.info("Загружены жанры: {}.", film.get());
            film.get().setMpa(mpaDbStorage.findFilmMpa(id));
            log.info("Загружен mpa: {}.", film.get());
            return film;
        }
        return Optional.empty();
    }

    @Override
    public List<Film> findAll() {
        return addFilmsProperties(super.findAll());
    }

    @Override
    public List<Film> findTopByLikes(Long limit) {
        var sql = sqlQuery + " group by f.id order by rate desc limit = " + limit;
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    @Override
    public Film addLike(long k1, long k2) throws EntityNotFoundException {
        Film v = findById(k1).orElseThrow(
                () -> new EntityNotFoundException("Film with Id: " + k1 + " not found")
        );
        SqlRowSet favoriteFilmsRows = jdbcTemplate.queryForRowSet(
                "select * from user_film_like " +
                        "where film_id = ? " +
                        "and user_id = ?", k1, k2);
        int rate = v.getRate();
        if (!favoriteFilmsRows.next()) {
            String sqlQuery = "insert into user_film_like(film_id, user_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, k1, k2);
            rate = rate + 1;
            v.setRate(rate);
        }
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, rate
        );
        return v;
    }

    @Override
    public Film deleteLike(long k1, long k2) throws EntityNotFoundException {
        Film v = findById(k1).orElseThrow(
                () -> new EntityNotFoundException("Film with Id: " + k1 + " not found")
        );
        String sqlQuery = "delete from user_film_like where film_id = ? and user_id = ?";
        boolean b1 = jdbcTemplate.update(sqlQuery, k1, k2) > 0;
        if (!b1) {
            log.warn(
                    "Error! Cannot delete user Id: {} like, user like not found.",
                    k2
            );
            throw new EntityNotFoundException("Error! Cannot delete user Id: "
                    + k2 + " like, user like not found.");
        }
        int rate = v.getRate() - 1;
        v.setRate(rate);
        log.debug(
                "У фильма под Id: {} удален лайк от пользователя" +
                        " с Id: {}.\n Всего лайков: {}.",
                k1, k2, rate
        );
        return v;
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        var sql = "SELECT FILM.ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE, FILM.DURATION, " +
                "COUNT(L.USER_ID) as RATING " +
                "FROM FILM " +
                "JOIN user_film_like as L on FILM.ID = L.FILM_ID " +
                "JOIN user_film_like as L1 on L.FILM_ID = L1.FILM_ID " +
                "WHERE L.user_id = " + userId +
                " AND L1.user_id = " + friendId +
                " GROUP BY FILM.ID " +
                "ORDER BY RATING DESC";
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    private void saveFilmProperties(Film film) {
        var filmId = film.getId();
        genreDbStorage.saveFilmGenres(filmId, film.getGenres());
        mpaDbStorage.saveFilmMpa(filmId, film.getMpa().getId());
    }

    private List<Film> addFilmsProperties(List<Film> films) {
        for (Film film : films) {
            var id = film.getId();
            film.setGenres(genreDbStorage.findFilmGenres(id));
            film.setMpa(mpaDbStorage.findFilmMpa(id));
        }
        return films;
    }
}