package ru.koChyan.Auction.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class GreetingController {

    @GetMapping()
    public String greeting(){

       return "greeting";
    }



}
