package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.LotRepo;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {

    private final LotRepo lotRepo;

    @Autowired
    public MainController(LotRepo lotRepo) {
        this.lotRepo = lotRepo;
    }

    @GetMapping("/")
    public String greeting(Model model){
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(name = "filter", required = false, defaultValue = "") String filter, Model model) {
        Iterable<Lot> lots;

        //если фильтр не пустой и не null, то ищем по фильтру
        //если фильтр пустой (не задан), то выводим все лоты
        if (filter != null && !filter.isEmpty()) {
            lots = lotRepo.findByName(filter);
        } else {
            lots = lotRepo.findAll();
        }

        model.addAttribute("lots", lots);
        return "main";
    }

}
