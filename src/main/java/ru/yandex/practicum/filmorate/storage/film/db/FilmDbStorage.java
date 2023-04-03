package ru.yandex.practicum.filmorate.storage.film.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.EntityMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private final EntityMapper<Film> mapper;
    private final GenreDbStorage genreDbStorage;
    private final MPADbStorage mpaDbStorage;
    private final LikesDbStorage likesDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         EntityMapper<Film> mapper,
                         GenreDbStorage genreDbStorage,
                         MPADbStorage mpaDbStorage,
                         LikesDbStorage likesDbStorage) {
        super(jdbcTemplate, mapper);
        this.mapper = mapper;
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
            film.get().setLikes(likesDbStorage.findFilmLikes(id));
            log.info("Загружены лайки: {}.", film.get());
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