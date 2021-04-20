package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.response.TimerResponse;
import ru.koChyan.Auction.service.BetService;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.PricingService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lot/{lot}/bet")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @Autowired
    private LotService lotService;

    @Autowired
    private BetService betService;

    @Autowired
    private SimpMessagingTemplate template;


    @GetMapping()
    public String betsList(
            @PathVariable Lot lot,
            Model model
    ) {

        model.addAttribute("timeBefore", betService.getTimeBefore(lot));
        model.addAttribute("timeLeft", betService.getTimeLeft(lot));
        model.addAttribute("lot", lot);
        model.addAttribute("pricing", pricingService.findLastThreeByLotId(lot.getId()));
        return "bet/addBet";
    }

    @PostMapping()
    public String addPricing(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot,
            @RequestParam(name = "bet", required = false, defaultValue = "0") Long bet,
            @RequestParam(name = "date") String date,
            Model model
    ) {
        BindingResult bindingResult = Validator.justGreater(lot, bet);

        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("timeBefore", betService.getTimeBefore(lot));
            model.addAttribute("timeLeft", betService.getTimeLeft(lot));
            model.addAttribute("lot", lot);
            model.addAttribute("pricing", pricingService.findLastThreeByLotId(lot.getId()));

            return "bet/addBet";
        } else {

            pricingService.addPrice(user, lot, bet, new Date(date));
            lotService.updateLastBet(lot, bet);

            Map<String, String> pricingJson = new HashMap<>();
            pricingJson.put("bet", String.valueOf(bet));
            pricingJson.put("date", date);
            pricingJson.put("username", user.getUsername());

            template.convertAndSend(
                    "/topic/bets/" + lot.getId(),
                    pricingJson
            );

            return "redirect:/lot/" + lot.getId() + "/bet";
        }
    }


    //@MessageMapping("/pricing")
    //@SendTo("/topic/bets")
    //public PricingResponse showLastBet(Pricing pricing) {
    //    System.out.println(pricing.getBet());

    //    return new PricingResponse(HtmlUtils.htmlEscape((pricing.getBet()) + ""));
    //}

    @Scheduled(fixedDelay = 1000)
    public void showTimer() {
        template.convertAndSend("/topic/timer", new TimerResponse(String.valueOf(new Date().getTime())));
    }

}

