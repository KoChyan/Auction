package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.koChyan.Auction.service.LotService;

@Controller
public class GreetingController {
    @Autowired
    private LotService lotService;


    @GetMapping("/")
    public String greeting(Model model){
        return "greeting";
    }

}
