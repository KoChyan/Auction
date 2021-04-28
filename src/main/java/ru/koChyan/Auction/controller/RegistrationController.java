package ru.koChyan.Auction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.domain.dto.UserDto;
import ru.koChyan.Auction.domain.dto.response.CaptchaResponseDto;
import ru.koChyan.Auction.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;


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

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Заполните капчу");
        }

        if (bindingResult.hasErrors() || !response.isSuccess()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("user", userDto);

            return "registration";
        } else {
            //Если при добавлении пользователя произойдет ошибка (получим false)
            //то оповестим об этом
            if (!userService.addUser(userDto)) {
                model.addAttribute("usernameError", Arrays.asList("Ошибка при добавлении пользователя"));
                return "registration";
            }
            model.addAttribute("message", "Перейдите по ссылке из письма на вашей почте для активации аккаунта");
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
            model.addAttribute("message", "Аккаунт успешно активирован!");
        } else {
            model.addAttribute("message", "Код активации не был найден!");
        }
        return "/login";
    }
}
