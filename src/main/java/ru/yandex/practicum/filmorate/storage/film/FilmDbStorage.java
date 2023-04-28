package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
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
    private final LikesDbStorage likesDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         EntityMapper<Film> mapper,
                         GenreDbStorage genreDbStorage,
                         MPADbStorage mpaDbStorage,
                         LikesDbStorage likesDbStorage) {
        super(jdbcTemplate, mapper);
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.likesDbStorage = likesDbStorage;
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
        Optional<Film> film = super.findById(id);
        if (film.isPresent()) {
            film.get().setGenres(genreDbStorage.findFilmGenres(id));
            log.info("Загружены жанры: {}.", film.get());
            film.get().setMpa(mpaDbStorage.findFilmMpa(id));
            log.info("Загружен mpa: {}.", film.get());
            var likes = likesDbStorage.findFilmLikes(id);
            film.get().setLikes(likes);
            log.info("Загружены лайки: {}.", film.get());
            film.get().setRate(likes.size());
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
        var sql = "SELECT ID, " +
                getFieldsSeparatedByCommas() +
                " FROM " + mapper.getTableName() +
                " WHERE id IN " +
                "(SELECT film_id " +
                "FROM user_film_like " +
                "ORDER BY user_id DESC " +
                "LIMIT " + limit + ")";
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
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
        likesDbStorage.saveFilmLikes(filmId, film.getLikes());
    }

    private List<Film> addFilmsProperties(List<Film> films) {
        for (Film film : films) {
            var id = film.getId();
            film.setGenres(genreDbStorage.findFilmGenres(id));
            film.setMpa(mpaDbStorage.findFilmMpa(id));
            var likes = likesDbStorage.findFilmLikes(id);
            film.setLikes(likes);
            film.setRate(likes.size());
        }
        return films;
    }
}