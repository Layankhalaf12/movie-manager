// Student Name: Layan Khalaf | Student ID: 991274007
package ca.khalaf.a2layan.service;

import ca.khalaf.a2layan.data.MovieRepository;
import ca.khalaf.a2layan.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    void seedIfEmpty() {
        if (movieRepository.count() > 0) {
            return;
        }

        movieRepository.save(new Movie("Spirited Away", "Hayao Miyazaki", 2001, 10, 125, "Animation"));
        movieRepository.save(new Movie("The Dark Knight", "Christopher Nolan", 2008, 9, 152, "Action"));
        movieRepository.save(new Movie("Parasite", "Bong Joon-ho", 2019, 9, 132, "Thriller"));
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll(Sort.by("title").ascending());
    }

    public Movie getMovieById(Integer id) {
        if (id == null) {
            return null;
        }
        return movieRepository.findById(id).orElse(null);
    }

    public String addMovie(Movie movie) {
        normalize(movie);
        String error = applyBusinessRules(movie);
        if (error != null) {
            return error;
        }
        movieRepository.save(movie);
        return null;
    }

    public String updateMovie(Movie movie) {
        if (movie == null || movie.getId() == null) {
            return "Movie id is required.";
        }

        Movie existing = movieRepository.findById(movie.getId()).orElse(null);
        if (existing == null) {
            return "Movie not found.";
        }

        normalize(movie);
        String error = applyBusinessRules(movie);
        if (error != null) {
            return error;
        }

        existing.setTitle(movie.getTitle());
        existing.setDirector(movie.getDirector());
        existing.setReleaseYear(movie.getReleaseYear());
        existing.setGenre(movie.getGenre());
        existing.setRating(movie.getRating());
        existing.setDurationMinutes(movie.getDurationMinutes());

        movieRepository.save(existing);
        return null;
    }

    public List<Movie> filterMovies(String title, String genre, Integer minRating) {
        String titleFilter = normalizeFilter(title);
        String genreFilter = normalizeFilter(genre);

        if (titleFilter == null && genreFilter == null && minRating == null) {
            return List.of();
        }

        return movieRepository.filterMovies(titleFilter, genreFilter, minRating);
    }

    private String normalizeFilter(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public String deleteMovieById(Integer id) {
        if (id == null || id <= 0) {
            return "Invalid movie id.";
        }
        if (!movieRepository.existsById(id)) {
            return "Movie not found.";
        }
        movieRepository.deleteById(id);
        return null;
    }

    private void normalize(Movie movie) {
        if (movie == null) {
            return;
        }
        if (movie.getTitle() != null) {
            movie.setTitle(movie.getTitle().trim());
        }
        if (movie.getDirector() != null) {
            movie.setDirector(movie.getDirector().trim());
        }
        if (movie.getGenre() != null) {
            movie.setGenre(movie.getGenre().trim());
        }
    }

    private String applyBusinessRules(Movie movie) {
        if (movie == null) {
            return "Movie details are required.";
        }
        Integer releaseYear = movie.getReleaseYear();
        if (releaseYear != null) {
            int currentYear = Year.now().getValue();
            if (releaseYear > currentYear) {
                return "Release year cannot be in the future.";
            }
        }
        return null;
    }
}
