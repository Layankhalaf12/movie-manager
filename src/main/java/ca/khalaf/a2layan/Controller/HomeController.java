package ca.khalaf.a2layan.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping("/") // localhost:8080/
    public String home(Model model, @RequestParam String name , @RequestParam long number) {
    String message = "Welcome to our Movie Page "+ name + "!" + " Your student number is " + number + ".";
        model.addAttribute("message", message);
        return "index";
    }
    // home
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("currentPage", "home"); // Mark "home" as current
        return "redirect:/"; // Thymeleaf template namee
    }
// movies
    @GetMapping("/movies")
    public String about(Model model) {
        model.addAttribute("currentPage", "movie"); // Mark "movie" as currerent
        return "movies";
    }
// add movie
    @GetMapping("/add")
    public String contact(Model model) {
        model.addAttribute("currentPage", "contact"); // Mark "contact" as current
        return "addmovie";
    }
    // search movie
    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("currentPage", "search"); // Mark "search" as current
        return "search";
    }
}
