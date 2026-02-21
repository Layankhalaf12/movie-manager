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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("currentPage", "movies");
        return "movies";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("currentPage", "add");
        return "addmovie";
    }

    @PostMapping("/add")
    public String addMovie(
            @Valid @ModelAttribute("movie") Movie movie,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentPage", "add");
            return "addmovie";
        }

        try {
            movieService.addMovie(movie);
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("movie.error", ex.getMessage());
            model.addAttribute("currentPage", "add");
            return "addmovie";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Movie added successfully.");
        return "redirect:/movies";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam(value = "q", required = false) String query, Model model) {
        List<Movie> results = movieService.search(query);
        model.addAttribute("query", query);
        model.addAttribute("results", results);
        model.addAttribute("currentPage", "search");
        return "search";
    }
}
