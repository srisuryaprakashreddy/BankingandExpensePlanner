package com.Finance.BankingandExpensePlanner.controller;

import com.Finance.BankingandExpensePlanner.model.User;
import com.Finance.BankingandExpensePlanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user, Authentication authentication) {
        User existingUser = userService.findByUsername(authentication.getName()).orElse(null);
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            userService.save(existingUser);
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null || !userService.checkPassword(user, currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Current password entered is wrong.");
            return "redirect:/user/profile";
        }
        userService.updatePassword(user, newPassword);
        redirectAttributes.addFlashAttribute("successLogin", "Password changed successfully. Please log in again.");
        return "redirect:/login";
    }

    @PostMapping("/change-pin")
    public String changePin(
            @RequestParam String currentPin,
            @RequestParam String newPin,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null || !userService.checkPin(user, currentPin)) {
            redirectAttributes.addFlashAttribute("pinError", "Current PIN entered is wrong.");
            return "redirect:/user/profile";
        }
        userService.updatePin(user, newPin);
        redirectAttributes.addFlashAttribute("pinSuccessLogin", "PIN changed successfully. Please log in again.");
        return "redirect:/login";
    }
}
