package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.domain.Role;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.UserDto;
import ru.koChyan.Auction.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list")
    public String userList(Model model) {

        model.addAttribute("users", userService.findAll());
        return "user/userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{user}")
    public String userEdit(
            Model model,
            @PathVariable User user
    ) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "user/userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public String userSave(
            @RequestParam(name = "username") String username,
            @RequestParam() Map<String, String> form,
            @RequestParam(name = "balance") Long balance,
            @RequestParam(name = "userId") User user
    ) {

        userService.saveUser(user, username, form, balance);
        return "redirect:/user/list";
    }

    @GetMapping("/profile")
    public String getProfile(
            Model model,
            @AuthenticationPrincipal User user
    ) {
        Optional<User> optionalUser = userService.getById(user.getId());

        model.addAttribute("user", optionalUser.orElse(user));
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            Model model,
            @Valid UserDto userFromForm,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.addAttribute("user", user);
            model.mergeAttributes(errors);
            return "user/profile";

        } else {

            userService.updateProfile(user, userFromForm.getPassword(), userFromForm.getEmail());
            return "redirect:/user/profile";
        }
    }

}