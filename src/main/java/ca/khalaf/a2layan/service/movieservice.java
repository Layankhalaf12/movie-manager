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

    public Movie addMovie(Movie movie) {
        normalize(movie);
        applyBusinessRules(movie);
        return movieRepository.save(movie);
    }

    public List<Movie> search(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        return movieRepository.search(query.trim());
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

    private void applyBusinessRules(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie details are required.");
        }
        Integer releaseYear = movie.getReleaseYear();
        if (releaseYear != null) {
            int currentYear = Year.now().getValue();
            if (releaseYear > currentYear) {
                throw new IllegalArgumentException("Release year cannot be in the future.");
            }
        }
    }
}
