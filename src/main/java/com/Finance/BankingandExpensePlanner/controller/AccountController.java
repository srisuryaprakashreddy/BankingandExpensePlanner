package com.Finance.BankingandExpensePlanner.controller;


import com.Finance.BankingandExpensePlanner.model.Account;
import com.Finance.BankingandExpensePlanner.model.User;
import com.Finance.BankingandExpensePlanner.service.AccountService;
import com.Finance.BankingandExpensePlanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String accounts(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user != null) {
            model.addAttribute("accounts", accountService.getAccountsByUser(user));
            model.addAttribute("account", new Account());
        }
        return "accounts";
    }

    @PostMapping("/add")
    public String addAccount(@ModelAttribute Account account, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user != null) {
            account.setUser(user);
            accountService.saveAccount(account);
        }
        return "redirect:/accounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return "redirect:/accounts";
    }
}
