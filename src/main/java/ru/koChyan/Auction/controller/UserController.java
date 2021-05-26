package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Role;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.UserDto;
import ru.koChyan.Auction.service.SubscriptionService;
import ru.koChyan.Auction.service.UserService;
import ru.koChyan.Auction.validator.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;


@Controller
@RequestMapping("/user")
public class UserController {

    private final int SUBSCRIPTION_PAGE_SIZE = 6; // размер по умолчанию для страницы подписок

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ResourceBundle resourceBundle;

    @InitBinder("userDto")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(userValidator);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list")
    public String userList(
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        model.addAttribute("url", "/user/list");
        model.addAttribute("page", userService.getAll(pageable));
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
            @RequestParam() Map<String, String> form,
            @RequestParam(name = "balance") Long balance,
            @RequestParam(name = "userId") User user
    ) {

        userService.saveUser(user, form, balance);
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

            model.mergeAttributes(errors);
        } else {

            model.addAttribute("message", resourceBundle.getString("message.followLinkInEmail"));
            userService.updateProfile(user, userFromForm);
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/profile/subscriptions")
    public String getSubscriptions(
            @PageableDefault(size = SUBSCRIPTION_PAGE_SIZE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            Model model
    ) {

        model.addAttribute("page", subscriptionService.getAllLotsFor(user.getId(), pageable));
        model.addAttribute("url", "/user/profile/subscriptions");
        return "user/userSubscriptions";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/profile/subscriptions/{lot}/unsubscribe")
    public String unsubscribe(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot
    ) {

        subscriptionService.removeSubscription(lot.getId(), user.getId());
        return "redirect:/user/profile/subscriptions";
    }

}