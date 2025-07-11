package com.Finance.BankingandExpensePlanner.controller;

import com.Finance.BankingandExpensePlanner.model.User;
import com.Finance.BankingandExpensePlanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
                               @RequestParam String pin,
                               RedirectAttributes redirectAttributes) {
        if (userService.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/register";
        }
        if (userService.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }
        if (pin == null || pin.length() != 4 || !pin.matches("\\d{4}")) {
            redirectAttributes.addFlashAttribute("error", "PIN must be exactly 4 digits");
            return "redirect:/register";
        }
        userService.registerUser(user, pin);
        redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
        return "redirect:/login";
    }
}
