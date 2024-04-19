package net.letsdank.platform.controller;

import lombok.AllArgsConstructor;
import net.letsdank.platform.repository.common.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {
    private final UserRepository userRepository;

    @GetMapping
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        System.out.println(userRepository.findAll());
        return "login";
    }
}
