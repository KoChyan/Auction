package ru.koChyan.Auction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.UserService;

@Controller
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        //Если при добавлении пользователя произойдет ошибка (получим false)
        //То оповестим ою этом
        if (!userService.addUser(user)) {
            model.addAttribute("message", "Такой пользователь уже существует!");
            return "registration";
        }
        model.addAttribute("message", "Перейдите по ссылке из письма на вашей почте для активации аккаунта");
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable() String code){
        boolean isActivated = userService.activateUser(code);

         if(isActivated){
             model.addAttribute("message","Аккаунт успешно активирован!");
         }else{
             model.addAttribute("message", "Код активации не был найден!");
         }
        return "/login";
    }
}
