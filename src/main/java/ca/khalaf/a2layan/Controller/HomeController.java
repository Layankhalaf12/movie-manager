// Student Name: Layan Khalaf | Student ID: 991274007
package ca.khalaf.a2layan.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"}) // localhost:8080/
    public String home(Model model) {
        String studentName = "Layan Khalaf";
        String studentNumber = "991274007";

        model.addAttribute("studentName", studentName);
        model.addAttribute("studentNumber", studentNumber);
        model.addAttribute("welcomeMessage", "Welcome to the Movie Library!");
        model.addAttribute("currentPage", "home");
        return "index";
    }
}
