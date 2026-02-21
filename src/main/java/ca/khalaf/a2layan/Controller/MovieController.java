// Student Name: Layan Khalaf | Student ID: 000000000
package ca.khalaf.a2layan.Controller;

import ca.khalaf.a2layan.model.Movie;
import ca.khalaf.a2layan.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public String listMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("moviesMessage",
                movies.isEmpty() ? "No movies yet. Add one to get started." : "");
        model.addAttribute("successMessage", "");
        model.addAttribute("errorMessage", "");
        model.addAttribute("currentPage", "movies");
        return "movies";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("formTitle", "Add Movie");
        model.addAttribute("submitLabel", "Add Movie");
        model.addAttribute("formAction", "/movies/add");
        model.addAttribute("globalErrorMessage", "");
        model.addAttribute("currentPage", "add");
        return "addmovie";
    }

    @PostMapping("/add")
    public String addMovie(
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Add Movie");
            model.addAttribute("submitLabel", "Add Movie");
            model.addAttribute("formAction", "/movies/add");
            model.addAttribute("globalErrorMessage", "");
            model.addAttribute("currentPage", "add");
            return "addmovie";
        }

        String addError = movieService.addMovie(movie);
        if (addError != null) {
            model.addAttribute("formTitle", "Add Movie");
            model.addAttribute("submitLabel", "Add Movie");
            model.addAttribute("formAction", "/movies/add");
            model.addAttribute("globalErrorMessage", addError);
            model.addAttribute("currentPage", "add");
            return "addmovie";
        }

        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("moviesMessage",
                movies.isEmpty() ? "No movies yet. Add one to get started." : "");
        model.addAttribute("successMessage", "Movie added successfully.");
        model.addAttribute("errorMessage", "");
        model.addAttribute("currentPage", "movies");
        return "movies";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@org.springframework.web.bind.annotation.PathVariable String id,
                               Model model) {
        Integer movieId = parseId(id);
        if (movieId == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "");
            model.addAttribute("errorMessage", "Invalid movie id.");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "");
            model.addAttribute("errorMessage", "Movie not found.");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }

        model.addAttribute("movie", movie);
        model.addAttribute("formTitle", "Edit Movie");
        model.addAttribute("submitLabel", "Save Changes");
        model.addAttribute("formAction", "/movies/edit");
        model.addAttribute("globalErrorMessage", "");
        model.addAttribute("currentPage", "movies");
        return "addmovie";
    }

    @PostMapping("/edit")
    public String editMovie(
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formTitle", "Edit Movie");
            model.addAttribute("submitLabel", "Save Changes");
            model.addAttribute("formAction", "/movies/edit");
            model.addAttribute("globalErrorMessage", "");
            model.addAttribute("currentPage", "movies");
            return "addmovie";
        }

        String updateError = movieService.updateMovie(movie);
        if (updateError != null) {
            model.addAttribute("formTitle", "Edit Movie");
            model.addAttribute("submitLabel", "Save Changes");
            model.addAttribute("formAction", "/movies/edit");
            model.addAttribute("globalErrorMessage", updateError);
            model.addAttribute("currentPage", "movies");
            return "addmovie";
        }

        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("moviesMessage",
                movies.isEmpty() ? "No movies yet. Add one to get started." : "");
        model.addAttribute("successMessage", "Movie updated successfully.");
        model.addAttribute("errorMessage", "");
        model.addAttribute("currentPage", "movies");
        return "movies";
    }

    @GetMapping("/search")
    public String searchMovies(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "minRating", required = false) String minRating,
            Model model
    ) {
        Integer minRatingValue = null;
        boolean invalidRating = false;
        if (minRating != null && !minRating.isBlank()) {
            minRatingValue = parseNonNegativeInt(minRating);
            if (minRatingValue == null || minRatingValue > 10) {
                invalidRating = true;
                minRatingValue = null;
            }
        }
        List<Movie> results = movieService.filterMovies(title, genre, minRatingValue);
        String searchMessage = "";
        if (invalidRating) {
            searchMessage = "Minimum rating must be a number between 0 and 10.";
        } else if ((title == null || title.isBlank())
                && (genre == null || genre.isBlank())
                && minRatingValue == null) {
            searchMessage = "Enter at least one filter to search.";
        } else if (results.isEmpty()) {
            searchMessage = "No results found.";
        }
        model.addAttribute("title", title);
        model.addAttribute("genre", genre);
        model.addAttribute("minRating", minRating);
        model.addAttribute("results", results);
        model.addAttribute("searchMessage", searchMessage);
        model.addAttribute("currentPage", "search");
        return "search";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@org.springframework.web.bind.annotation.PathVariable String id,
                                         Model model) {
        Integer movieId = parseId(id);
        if (movieId == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "");
            model.addAttribute("errorMessage", "Invalid movie id.");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "");
            model.addAttribute("errorMessage", "Movie not found.");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }
        model.addAttribute("movie", movie);
        model.addAttribute("currentPage", "movies");
        return "delete-confirm";
    }

    @PostMapping("/delete")
    public String deleteMovie(@RequestParam("id") String id, Model model) {
        Integer movieId = parseId(id);
        if (movieId == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "");
            model.addAttribute("errorMessage", "Invalid movie id.");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }
        String deleteError = movieService.deleteMovieById(movieId);
        if (deleteError == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "Movie deleted successfully.");
            model.addAttribute("errorMessage", "");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        model.addAttribute("moviesMessage",
                movies.isEmpty() ? "No movies yet. Add one to get started." : "");
        model.addAttribute("successMessage", "");
        model.addAttribute("errorMessage", deleteError);
        model.addAttribute("currentPage", "movies");
        return "movies";
    }

    @GetMapping("/details/{id}")
    public String viewMovieDetails(@org.springframework.web.bind.annotation.PathVariable String id,
                                   Model model) {
        Integer movieId = parseId(id);
        if (movieId == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "");
            model.addAttribute("errorMessage", "Invalid movie id.");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) {
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            model.addAttribute("moviesMessage",
                    movies.isEmpty() ? "No movies yet. Add one to get started." : "");
            model.addAttribute("successMessage", "");
            model.addAttribute("errorMessage", "Movie not found.");
            model.addAttribute("currentPage", "movies");
            return "movies";
        }
        model.addAttribute("movie", movie);
        model.addAttribute("currentPage", "movies");
        return "movie-details";
    }

    private Integer parseId(String id) {
        Integer value = parseNonNegativeInt(id);
        return (value == null || value <= 0) ? null : value;
    }

    private Integer parseNonNegativeInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String trimmed = value.trim();
        int result = 0;
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c < '0' || c > '9') {
                return null;
            }
            int digit = c - '0';
            if (result > (Integer.MAX_VALUE - digit) / 10) {
                return null;
            }
            result = result * 10 + digit;
        }
        return result;
    }

}
