DROP TABLE IF EXISTS film_director;
DROP TABLE IF EXISTS film_mpa;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS user_friend;
DROP TABLE IF EXISTS user_film_like;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS mpa_rating;
DROP TABLE IF EXISTS director;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS usr;
DROP TABLE IF EXISTS film;

CREATE TABLE IF NOT EXISTS film
(
    id           bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         varchar,
    description  varchar,
    release      timestamp,
    duration     int
);

CREATE TABLE IF NOT EXISTS usr
(
    id       bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    varchar,
    login    varchar,
    name     varchar,
    birthday timestamp
);

CREATE TABLE IF NOT EXISTS user_film_like
(
    film_id bigint REFERENCES usr (id) ON DELETE CASCADE,
    user_id bigint REFERENCES film (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genre
(
    id   bigint PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS director
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS mpa_rating
(
    id   bigint PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS status
(
    id   bigint PRIMARY KEY,
    name varchar
);

CREATE TABLE IF NOT EXISTS film_mpa
(
    film_id bigint REFERENCES film (id) ON DELETE CASCADE,
    mpa_id  bigint REFERENCES mpa_rating (id)
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  bigint REFERENCES film (id) ON DELETE CASCADE,
    genre_id bigint REFERENCES genre (id)
);

CREATE TABLE IF NOT EXISTS user_friend
(
    user_id         bigint REFERENCES usr (id) ON DELETE CASCADE,
    user_friend_id  bigint REFERENCES usr (id) ON DELETE CASCADE,
    status_id       bigint REFERENCES status (id)
);

CREATE TABLE IF NOT EXISTS film_director
(
    film_id     bigint,
    director_id bigint,
    FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE,
    FOREIGN KEY (director_id) REFERENCES director (id) ON DELETE CASCADE
);