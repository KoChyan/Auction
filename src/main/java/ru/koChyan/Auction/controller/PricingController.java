package ru.koChyan.Auction.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.controller.util.validator.LotValidator;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Status;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.LotStatusResponseDto;
import ru.koChyan.Auction.domain.dto.TimerResponseDto;
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
    public String betList(
            @PathVariable Lot lot,
            Model model
    ) {
        // если лот не имеет статус "активен"
        // то редиректим со страницы торгов этого лота на главную
        if (lot.getStatus().equals("ACTIVE")) {

            model.addAttribute("timeBefore", betService.getTimeBefore(lot));
            model.addAttribute("timeLeft", betService.getTimeLeft(lot));
            model.addAttribute("lot", lot);
            model.addAttribute("pricing", pricingService.getLastByLotId(lot.getId()));
            return "bet/addBet";
        }

        return "redirect:/lot";
    }

    @PostMapping()
    public String addPricing(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot,
            @RequestParam(name = "bet", required = false, defaultValue = "0") Long bet,
            @RequestParam(name = "date") String date,
            Model model
    ) {
        BindingResult bindingResult = LotValidator.justGreater(lot, bet);

        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("timeBefore", betService.getTimeBefore(lot));
            model.addAttribute("timeLeft", betService.getTimeLeft(lot));
            model.addAttribute("lot", lot);
            model.addAttribute("pricing", pricingService.getLastByLotId(lot.getId()));

            return "bet/addBet";
        } else {

            pricingService.addPrice(user, lot, bet, new Date(date));
            lotService.updateLastBet(lot, bet);

            Map<String, String> pricing = new HashMap<>();
            pricing.put("bet", String.valueOf(bet));
            pricing.put("date", date);
            pricing.put("username", user.getUsername());

            template.convertAndSend("/topic/bets/" + lot.getId(), pricing);

            return "redirect:/lot/" + lot.getId() + "/bet";
        }
    }

    @MessageMapping("/pricing")
    public void getStatusUpdate(
            String responseJson
    ) {
        LotStatusResponseDto lotStatusResponseDto = new Gson().fromJson(responseJson, LotStatusResponseDto.class);

        if (lotStatusResponseDto.getStatus().toLowerCase().equals("finished"))
           lotService.updateStatus(lotStatusResponseDto.getId(), Status.FINISHED.name());
    }

    @Scheduled(fixedDelay = 1000)
    public void showTimer() {
        template.convertAndSend("/topic/timer", new TimerResponseDto(String.valueOf(new Date().getTime())));
    }

}
