package ca.khalaf.a2layan.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title is required.")
    @Size(max = 100, message = "Title must be 100 characters or less.")
    private String title;

    @NotBlank(message = "Director is required.")
    @Size(max = 100, message = "Director must be 100 characters or less.")
    private String director;

    @NotNull(message = "Release year is required.")
    @Min(value = 1888, message = "Release year must be 1888 or later.")
    private Integer releaseYear;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be between 1 and 10.")
    @Max(value = 10, message = "Rating must be between 1 and 10.")
    private Integer rating;

    @NotNull(message = "Duration is required.")
    @Min(value = 1, message = "Duration must be at least 1 minute.")
    private Integer duration; // in minutes

    @NotBlank(message = "Genre is required.")
    @Size(max = 50, message = "Genre must be 50 characters or less.")
    private String genre;

    public Movie() {
        // Required by JPA
    }

    public Movie(String title, String director, Integer releaseYear, Integer rating, Integer duration, String genre) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.duration = duration;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", title=" + title + ", director=" + director + ", releaseYear=" + releaseYear
                + ", rating=" + rating + ", duration=" + duration + ", genre=" + genre + "]";
    }
}
