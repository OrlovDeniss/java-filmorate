DROP TABLE IF EXISTS feed;
DROP TABLE IF EXISTS user_review_like;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS film_mpa;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS user_friend;
DROP TABLE IF EXISTS user_film_like;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS mpa_rating;
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
    id      bigint GENERATED BY DEFAULT AS IDENTITY,
    film_id bigint REFERENCES usr (id) ON DELETE CASCADE,
    user_id bigint REFERENCES film (id) ON DELETE CASCADE,
    PRIMARY KEY(id, film_id, user_id)
);

CREATE TABLE IF NOT EXISTS genre
(
    id   bigint PRIMARY KEY,
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

CREATE TABLE IF NOT EXISTS reviews
(
    id          bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content     varchar,
    is_positive boolean,
    user_id     bigint,
    film_id     bigint,
    CONSTRAINT fk_r_user_id FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE,
    CONSTRAINT fk_r_film_id FOREIGN KEY (film_id) REFERENCES film (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_review_like
(
    review_id bigint,
    user_id bigint,
    is_like boolean,
    CONSTRAINT fk_url_review_id FOREIGN KEY (review_id) REFERENCES reviews (id) ON DELETE CASCADE,
    CONSTRAINT fk_url_user_id FOREIGN KEY (user_id)   REFERENCES usr (id) ON DELETE CASCADE,
    PRIMARY KEY (review_id, user_id)
);

CREATE TABLE IF NOT EXISTS feed
(
    id             bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    feed_timestamp bigint,
    user_id        bigint,
    event_type     varchar(32),
    operation      varchar(32),
    entity_id      bigint,
    CONSTRAINT feed_user_id FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE
 );