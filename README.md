# java-filmorate
Template repository for Filmorate project.

### Тема 4/8: Промежуточное задание месяца SQL
https://dbdiagram.io/d/64196a81296d97641d8985fa


<details>
<summary>dbdiagram.io code</summary>
  
  
    Table film {
      id bigint [pk]
      name varchar
      description varchar(200)
      release datetime
      duration int
    }

    Table user_film_like {
      user_id bigint [ref: - usr.id]
      film_id bigint [ref: - film.id]
    }

    Table usr {
      id bigint [pk]
      email varchar
      login varchar
      name varchar
      birthday datetime
    }

    Table user_frend {
      user_id bigint [ref: - usr.id]
      user_firend_id bigint [ref: > usr.id]
      status_id int
    }

    Table status {
      id int [ref: - user_frend.status_id]
      name varchar
    }

    Table film_genre {
      film_id bigint [ref: - film.id]
      genre_id int [ref: > genre.id]
    }

    Table genre {
      id int [pk]
      name varchar
    }

    Table film_mpa {
      film_id bigint [ref: - film.id]
      mpa_id int [ref: - mpa_rating.id]
    }

    Table mpa_rating {
      id int
      name varchar
    }
</details>

![Image alt](https://github.com/OrlovDeniss/java-filmorate/blob/add-database/filmorate_ER.png)
