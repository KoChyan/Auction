package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.PricingService;

import java.io.IOException;

@Controller
@RequestMapping("/lot")
public class LotController {
    @Value("${upload.path}")
    private String uploadPath;

    private final LotService lotService;
    private final PricingService pricingService;

    @Autowired
    public LotController(LotService lotService, PricingService pricingService) {
        this.lotService = lotService;
        this.pricingService = pricingService;
    }

    @GetMapping("/add")
    public String lotForm(Model model) {
        return "creatingLot";
    }

    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "initialRate") Double initialRate,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "timeStep") Double timeStep,
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException {
        lotService.addLot(user, name, description, initialRate, startDate, timeStep, file);

        return "redirect:/main";
    }

    @GetMapping("/{lot}")
    public String bettingPage(
            @PathVariable Lot lot,
            Model model
    ) {
        model.addAttribute("lot", lot);
        model.addAttribute("pricing", pricingService.findLastThreeByLotId(lot.getId()));
        return "lotBet";
    }
}
