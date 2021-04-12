package ru.koChyan.Auction.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class GreetingController {

    @GetMapping("/")
    public String greeting(Model model){

       return "greeting";
    }



}
