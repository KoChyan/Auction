package ru.koChyan.Auction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.domain.dto.UserDto;
import ru.koChyan.Auction.domain.dto.response.CaptchaResponseDto;
import ru.koChyan.Auction.service.UserService;
import ru.koChyan.Auction.validator.UserValidator;

import javax.validation.Valid;
import java.util.*;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Autowired
    private ResourceBundle resourceBundle;

    @Autowired
    private UserValidator userValidator;

    @InitBinder("userDto")
    protected void initBinder(WebDataBinder binder){
        binder.addValidators(userValidator);
    }


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            Model model,
            @Valid UserDto userDto,
            BindingResult bindingResult
    ) {

        String url = String.format(CAPTCHA_URL, recaptchaSecret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!Objects.requireNonNull(response).isSuccess()) {
            model.addAttribute("captchaError", resourceBundle.getString("error.captcha"));
        }

        if (bindingResult.hasErrors() || !Objects.requireNonNull(response).isSuccess()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("user", userDto);

            return "registration";
        } else {
            userService.addUser(userDto);
            model.addAttribute("message", resourceBundle.getString("message.followLinkInEmail"));
            return "login";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(
            Model model,
            @PathVariable String code
    ) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", resourceBundle.getString("message.activationCode.valid"));
        } else {
            model.addAttribute("message", resourceBundle.getString("message.activationCode.invalid"));
        }
        return "login";
    }
}
