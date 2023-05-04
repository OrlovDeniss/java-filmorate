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

    private final GenreDbStorage genreStorage;
    private final MPADbStorage mpaStorage;
    private final FilmLikesDbStorage likesStorage;
    private final FeedDbStorage feedStorage;
    private final DirectorDbStorage directorStorage;
    private final String sqlQuery = "with l as" +
            " (select film_id, avg(like_rate) as lc" +
            " from user_film_like" +
            " group by film_id)" +
            " select id, " +
            getFieldsSeparatedByCommas() +
            ", l.lc as rate" +
            " from " + mapper.getTableName() + " as f" +
            " left join l on l.film_id = f.id";

    private final String sqlQueryWithProperty = sqlQuery + " LEFT JOIN film_mpa AS fm ON fm.film_id = f.id" +
            " LEFT JOIN film_genre AS fg ON fg.film_id = f.id";

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         EntityMapper<Film> mapper,
                         GenreDbStorage genreStorage,
                         MPADbStorage mpaStorage,
                         FilmLikesDbStorage likesStorage,
                         FeedDbStorage feedStorage,
                         DirectorDbStorage directorStorage) {
        super(jdbcTemplate, mapper);
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
        this.likesStorage = likesStorage;
        this.feedStorage = feedStorage;
        this.directorStorage = directorStorage;
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
            film.get().setGenres(genreStorage.findFilmGenres(id));
            log.info("Загружены жанры: {}.", film.get());
            film.get().setMpa(mpaStorage.findFilmMpa(id));
            log.info("Загружен mpa: {}.", film.get());
            film.get().setDirectors(directorStorage.findFilmDirector(id));
            log.info("Загружен directors: {}.", film.get());
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
    public List<Film> findTopByLikes(Long limit, Long genreId, Long year) {
        String sql1 = " ";
        if (genreId != null && year != null) {
            sql1 = " WHERE YEAR(f.release) = " + year + " AND " +
                    " fg.genre_id = " + genreId;
        } else if (genreId != null) {
            sql1 = " WHERE fg.genre_id = " + genreId;
        } else if (year != null) {
            sql1 = " WHERE YEAR(f.release) = " + year;
        }
        String sql = sqlQueryWithProperty + sql1 + " GROUP BY f.id" +
                " ORDER BY rate DESC" +
                " LIMIT " + limit;
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    @Override
    public Film addLike(long filmId, long userId, int rate) throws EntityNotFoundException {

        likesStorage.addLike(filmId, userId, rate);
        feedStorage.saveUserFeed(Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(EventType.LIKE)
                .operation(OperationType.ADD)
                .entityId(filmId)
                .build());
        log.debug(
                "Фильм под Id: {} получил лайк от пользователя с Id: {}",
                filmId, userId
        );
        return findById(filmId).get();
    }

    @Override
    public Film deleteLike(long filmId, long userId) throws EntityNotFoundException {

        likesStorage.deleteLike(filmId, userId);
        feedStorage.saveUserFeed(Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(EventType.LIKE)
                .operation(OperationType.REMOVE)
                .entityId(filmId)
                .build());
        log.debug(
                "У фильма под Id: {} удален лайк от пользователя с Id: {}.",
                filmId, userId
        );
        return findById(filmId).get();
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        var sql = "SELECT FILM.ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE, FILM.DURATION, " +
                "AVG(L.like_rate) as RATE " +
                "FROM FILM " +
                "JOIN user_film_like as L on FILM.ID = L.FILM_ID " +
                "JOIN user_film_like as L1 on L.FILM_ID = L1.FILM_ID " +
                "WHERE L.user_id = " + userId +
                " AND L1.user_id = " + friendId +
                " GROUP BY FILM.ID " +
                "ORDER BY RATE DESC";
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    @Override
    public List<Film> getDirectorFilmsSortBy(Long directorId, String sortBy) {
        directorStorage.containsOrElseThrow(directorId);
        var sql = sqlQuery +
                " WHERE ID IN " +
                "(SELECT FILM_ID" +
                " FROM FILM_DIRECTOR" +
                " WHERE DIRECTOR_ID = " + directorId + ")" +
                " ORDER BY " + sortBy;
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    @Override
    public List<Film> getFilmRecommendation(Long id) {
        String sql = sqlQuery +
                " where ID in (" + //алгоритм slope one(нет)
                "select FILM_ID from USER_FILM_LIKE " +
                "where USER_ID in (" + //id пользователя, который больше всего похож по оценкам фильмов
                "select other_user from(" + //вспомогательный селект
                "select user_match.u_id usr, user_match.other_user, avg(user_match.like_match) match_rate " +
                //выбираем показатели похожести пользователей по лайкам
                "from(" +
                "select ufl.FILM_ID film_id, ufl.USER_ID u_id, ufl2.USER_ID other_user, " +
                "case " + //считаем разницу в лайках
                "when (ufl.like_rate - UFL2.like_rate)<0 " +
                "then (ufl.like_rate - UFL2.like_rate)*-1 " +
                "else (ufl.like_rate - UFL2.like_rate) " +
                "end as like_match " +
                "from USER_FILM_LIKE ufl " + //присоединяем лайки к лайкам
                "left join USER_FILM_LIKE ufl2 on UFL2.FILM_ID = ufl.FILM_ID " +
                "where UFL2.USER_ID != ufl.USER_ID" +
                ") as user_match " +
                //выбираем интересующего нас пользователя
                "where user_match.u_id = " + id +
                " group by user_match.u_id, user_match.other_user " +
                "having match_rate is not null " +
                //ограничиваем данные о других пользователях до 1 самой похожей по оценкам
                "order by usr, match_rate limit 1)) " +
                //получаем список фильмов другого пользователя, которые не оценил исходный
                "and FILM_ID not in(select film_id from USER_FILM_LIKE where USER_ID = " + id + ") " +
                //с положительными оценками
                "and is_positive = true) " +
                "group by f.ID " +
                "order by RATE desc";
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    @Override
    public List<Film> searchByDirectorOrTitle(String word, String[] locationsForSearch) {
        String sql = "(SELECT FR.ID FROM (SELECT f.id, LOWER(f.name) AS name, f.description, " +
                "f.RELEASE, f.duration, r.rate FROM FILM f LEFT JOIN " +
                "(SELECT AVG(like_rate) AS RATE, film_id FROM USER_FILM_LIKE GROUP BY film_id) AS R " +
                "ON f.ID = r.film_id) AS FR LEFT JOIN (SELECT LOWER(D.NAME) AS NAME_DIRECTOR, " +
                "FD.FILM_ID FROM DIRECTOR D JOIN FILM_DIRECTOR FD ON D.ID = FD.DIRECTOR_ID) AS FDD " +
                "ON FR.ID = FDD.FILM_ID WHERE";
        if (locationsForSearch.length == 1) {
            if (locationsForSearch[0].equals("title")) {
                sql = sql + " FR.NAME LIKE '%" + word + "%')";
            } else {
                sql = sql + " FDD.NAME_DIRECTOR LIKE '%" + word + "%')";
            }
        } else {
            sql = sql + " FDD.NAME_DIRECTOR LIKE '%" + word + "%' OR FR.NAME LIKE '%" +
                    word + "%')";
        }
        sql = sqlQuery + " WHERE f.id IN " + sql + " ORDER BY RATE DESC";
        return addFilmsProperties(jdbcTemplate.query(sql, mapper));
    }

    private void saveFilmProperties(Film film) {
        var filmId = film.getId();
        genreStorage.saveFilmGenres(filmId, film.getGenres());
        mpaStorage.saveFilmMpa(filmId, film.getMpa().getId());
        directorStorage.saveFilmDirector(filmId, film.getDirectors());
    }

    private List<Film> addFilmsProperties(List<Film> films) {
        for (Film film : films) {
            var id = film.getId();
            film.setGenres(genreStorage.findFilmGenres(id));
            film.setMpa(mpaStorage.findFilmMpa(id));
            film.setDirectors(directorStorage.findFilmDirector(id));
        }
        return films;
    }
}
