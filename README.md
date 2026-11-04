# Movie Manager (Spring Boot + MySQL)

A Spring Boot MVC web app for managing a movie collection stored in a **MySQL** database. Built for a Java web development course at Sheridan College.

## Features

- **Full CRUD.** List, view details, add, edit and delete movies.
- **Database-backed.** Movies are stored in MySQL through Spring Data JPA (`JpaRepository`), and the table is generated from the `Movie` entity.
- **Seed data.** Three sample movies are inserted on first start if the table is empty.
- **Validation.** Bean Validation on every field: required title, director and genre with length limits, release year 1888 or later, rating from 0 to 10, and duration of at least 1 minute. Validation errors are shown on the form.
- **Business rules.** Input is normalized and checked in the service layer before saving.
- **Search.** A custom JPQL query filters by title, genre (case-insensitive partial match) and minimum rating, sorted by title.
- **Thymeleaf views.** Shared layout fragments, success and error messages, and a Bootstrap interface.

## Architecture

| Layer | Class / folder | Responsibility |
|---|---|---|
| Controller | `Controller/MovieController` | Routes under `/movies`: list, `add`, `edit/{id}`, `details/{id}`, `delete/{id}`, `search` |
| Service | `service/MovieService` | Seeding, normalization, business rules, add / update / delete / filter |
| Repository | `data/MovieRepository` | `JpaRepository<Movie, Integer>` plus the custom `filterMovies` JPQL query |
| Model | `model/Movie` | JPA entity with validation annotations |
| Views | `templates/` | Thymeleaf pages: `index`, `movies`, `addmovie`, `search` |

## Running

1. Start a local MySQL server.
2. Check the connection settings in `src/main/resources/application.properties`. The app uses the database `db5` and creates it if it doesn't exist.
3. Run:

```bash
./mvnw spring-boot:run
```

4. Open http://localhost:8080/movies

## Tech
Java · Spring Boot · Spring MVC · Spring Data JPA / Hibernate · MySQL · Thymeleaf · Bootstrap · Maven

## Reflection

The assignment improved my knowledge of the development of a Spring Boot and Thymeleaf application with a clear MVC overview. I added CRUD functionality to the movies and enabled the validation and filtering of the movies all the time with a stable Bootstrap user interface.

The ability to use fragments enabled me to save layout components, which kept the templates well structured and readable. The example of feedback on minor issues like input normalization and template layout was a way of illustrating how backend logic has the ability to affect the user interface.

In general, it  increased my confidence in writing a code structure and providing a whole working web application.
