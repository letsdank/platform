package net.letsdank.platform.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {
    @GetMapping
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}
