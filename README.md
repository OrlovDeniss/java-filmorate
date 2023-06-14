# Filmorate
Filmorate - база фильмов, приложение позволяет пользователям добавлять фильмы и отзывы,
находить лучшие фильмы и получить рекомендации на основе интересов пользователей.

## Стек
- Java 11
- Maven
- Spring Boot 2
- JDBC
- H2 Database

## API
Service URL: http://localhost:9090.  
Id текущего пользователя передается в заголовке запроса "X-Sharer-User-Id". 

<details>
  <summary>Пользователи /items</summary>
  <br>

- GET /users - получить список всех пользователей.
- GET /users/{id} - получить информациб о пользователе по его id.
- POST /users - добавить нового пользователя.
- PUT /users - обновить информацию о пользователе.
- PUT /users/{id}/friends/{friendId} - добавить пользователя {friendId} в друзья пользователя {id}.
- DELETE /users/{id}/friends/{friendId} - удалить пользователя {friendId} из друзей пользователя {id}.
- GET /users/{id}/friends - получить список друзей пользователя.
- GET /users/{id}/friends/common/{otherId} - получить список дбщих друзей двух пользователей.
  
</details>

<details>
  <summary>Фильмы /films</summary>
  <br>
  
- GET /films - получть список всех фильмов.
- GET /films/{id} - получить описание о фильме по его id.
- POST /films - добавить новый фильм.
- PUT /films - обновить информацию о фильме
- PUT /films/{id}/like/{userId} - добавить лайк фильму {id} от пользователя {userId}.
- DELETE /films/{id}/like/{userId} - удалить лайк фильму {id} от пользователя {userId}.
- GET /films/popular - получить список фильмов с наибольшим числом лайков.
- GET /films/friends - получиться список фильмов, понравивщихся друзьям.
  
</details>

<details>
  <summary>Жанры /genres</summary>
  <br>

- GET /genres - получить список доступных жанров для фильмов
- GET /genres/{id} - получить описание жанра по его id
  
</details>

<details>
  <summary>Рейтинг MPA /mpa</summary>
  <br>
  
- GET /mpa - получить список доступных возрастных рейтингов для фильмов
- GET /mpa/{id} - получить описание рейтинга по его id

</details>

## Сборка
1. Клонируйте репозиторий:
```Bash
git clone https://github.com/OrlovDeniss/java-filmorate.git
```
2. Перейдите в каталог проекта: 
```Bash
cd java-filmorate
```
3. Скомпилируйте исходные файлы:
```Bash
mvn clean package
```
## Статус проекта
Завершен.
