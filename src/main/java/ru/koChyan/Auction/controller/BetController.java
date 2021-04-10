package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.BetService;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.PricingService;

import java.util.Date;

@Controller
@RequestMapping("/lot/{lot}/bet")
public class BetController {

    @Autowired
    private PricingService pricingService;

    @Autowired
    private LotService lotService;

    @Autowired
    private BetService betService;


    @GetMapping()
    public String betsList(
            @PathVariable Lot lot,
            Model model
    ) {

        model.addAttribute("timeLeft", betService.getTimeLeft(lot));
        model.addAttribute("lot", lot);
        model.addAttribute("pricing", pricingService.findLastThreeByLotId(lot.getId()));

        return "bet/addBet";
    }

    @PostMapping()
    public String addBet(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot,
            @RequestParam(name = "bet") Long bet,
            @RequestParam(name = "date") String date,
            Model model
    ) {
        pricingService.addPrice(user, lot, bet, new Date(date));
        lotService.updateLastBet(lot, bet);

        return "redirect:/lot/" + lot.getId() + "/bet";
    }

}
