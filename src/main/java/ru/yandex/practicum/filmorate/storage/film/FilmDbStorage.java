package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.Feed;
import ru.yandex.practicum.filmorate.model.user.enums.EventType;
import ru.yandex.practicum.filmorate.model.user.enums.OperationType;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.EntityMapper;
import ru.yandex.practicum.filmorate.storage.user.FeedDbStorage;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private final GenreDbStorage genreDbStorage;
    private final MPADbStorage mpaDbStorage;
    private final FilmLikesDbStorage likesStorage;
    private final FeedDbStorage feedStorage;
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
                         MPADbStorage mpaDbStorage,
                         FilmLikesDbStorage likesStorage,
                         FeedDbStorage feedStorage) {
        super(jdbcTemplate, mapper);
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.likesStorage = likesStorage;
        this.feedStorage = feedStorage;
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
        String sql = sqlQuery + " group by f.id";
        List<Film> collection = jdbcTemplate.query(sql, mapper);
        log.debug(
                "Запрос списка {}'s успешно выполнен, всего {}'s: {}",
                "Film", "Film", collection.size()
        );
        return addFilmsProperties(collection);
    }

    @Override
    public List<Film> findTopByLikes(Long limit) {
        var sql = sqlQuery + " group by f.id order by rate desc limit " + limit;
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    @Override
    public Film addLike(long k1, long k2) throws EntityNotFoundException {
        Film v = findById(k1).orElseThrow(
                () -> new EntityNotFoundException("Film with Id: " + k1 + " not found")
        );
        int rate = v.getRate();
        Long l = likesStorage.addLike(k1, k2);
        if (l != null) {
            rate = rate + 1;
            v.setRate(rate);
            feedStorage.saveUserFeed(Feed.builder()
                    .timestamp(Instant.now().toEpochMilli())
                    .userId(k2)
                    .eventType(EventType.LIKE)
                    .operation(OperationType.ADD)
                    .entityId(k1)
                    .build());
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
        int rate = v.getRate();
        Long l = likesStorage.deleteLike(k1, k2);
        if (l != null) {
            feedStorage.saveUserFeed(Feed.builder()
                    .timestamp(Instant.now().toEpochMilli())
                    .userId(k2)
                    .eventType(EventType.LIKE)
                    .operation(OperationType.REMOVE)
                    .entityId(k1)
                    .build());
            rate = rate - 1;
            v.setRate(rate);
        }
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
                "COUNT(L.USER_ID) as RATE " +
                "FROM FILM " +
                "JOIN user_film_like as L on FILM.ID = L.FILM_ID " +
                "JOIN user_film_like as L1 on L.FILM_ID = L1.FILM_ID " +
                "WHERE L.user_id = " + userId +
                " AND L1.user_id = " + friendId +
                " GROUP BY FILM.ID " +
                "ORDER BY RATE DESC";
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