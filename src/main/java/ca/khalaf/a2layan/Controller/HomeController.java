package ca.khalaf.a2layan.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;

@Controller
public class HomeController {
    private final String studentName;
    private final String studentNumber;

    public HomeController(
            @Value("${app.student.name:Student Name}") String studentName,
            @Value("${app.student.number:Student Number}") String studentNumber
    ) {
        this.studentName = studentName;
        this.studentNumber = studentNumber;
    }

    @GetMapping({"/", "/home"}) // localhost:8080/
    public String home(Model model) {
        model.addAttribute("studentName", studentName);
        model.addAttribute("studentNumber", studentNumber);
        model.addAttribute("welcomeMessage", "Welcome to the Movie Library!");
        model.addAttribute("currentPage", "home");
        return "index";
    }
}
