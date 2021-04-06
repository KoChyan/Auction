package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.domain.Role;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.UserService;

import java.util.Map;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public String userList(Model model) {

        model.addAttribute("users", userService.findAll());
        return "user/userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{user}")
    public String userEdit(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public String userSave(
            @RequestParam(name = "username") String username,
            @RequestParam() Map<String, String> form,
            @RequestParam(name = "balance") Long balance,
            @RequestParam(name = "userId") User user,
            Model model) {

        userService.saveUser(user, username, form, balance);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam() String password,
            @RequestParam() String email
    ) {

        userService.updateProfile(user, password, email);
        return "redirect:/profile";
    }

}
