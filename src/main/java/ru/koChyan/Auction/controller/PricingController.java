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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.controller.util.validator.PricingValidator;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Status;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.PricingDto;
import ru.koChyan.Auction.domain.dto.response.LotStatusResponseDto;
import ru.koChyan.Auction.domain.dto.response.TimerResponseDto;
import ru.koChyan.Auction.service.BetService;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.PricingService;

import javax.validation.Valid;
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

    @Autowired
    private PricingValidator pricingValidator;

    @InitBinder("pricingDto")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(pricingValidator);
    }


    @GetMapping()
    public String betList(
            @PathVariable Lot lot,
            Model model
    ) {
        // если лот не имеет статус "активен"
        // то редиректим со страницы торгов этого лота на главную
        if (lot.getStatus().equals(Status.ACTIVE.name())) {

            model.addAttribute("timeBefore", betService.getTimeBefore(lot));
            model.addAttribute("timeLeft", betService.getTimeLeft(lot));
            model.addAttribute("lot", lot);
            model.addAttribute("pricing", pricingService.getLastByLotId(lot.getId()));
            return "pricing/addBet";
        }

        return "redirect:/lot";
    }

    @PostMapping()
    public String addPricing(
            @AuthenticationPrincipal User user,
            @Valid PricingDto pricingDto,
            BindingResult bindingResult,
            @PathVariable Lot lot,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("timeBefore", betService.getTimeBefore(lot));
            model.addAttribute("timeLeft", betService.getTimeLeft(lot));
            model.addAttribute("lot", lot);
            model.addAttribute("pricing", pricingService.getLastByLotId(lot.getId()));

            return "pricing/addBet";
        } else {

            pricingService.addPrice(user, lot, pricingDto.getBet(), new Date(pricingDto.getDate()));
            lotService.updateLastBet(lot, pricingDto.getBet());

            Map<String, String> pricingInfo = new HashMap<>();
            pricingInfo.put("bet", String.valueOf(pricingDto.getBet()));
            pricingInfo.put("date", pricingDto.getDate());
            pricingInfo.put("username", user.getUsername());

            template.convertAndSend("/topic/bets/" + lot.getId(), pricingInfo);

            return "redirect:/lot/" + lot.getId() + "/bet";
        }
    }

    @MessageMapping("/pricing")
    public void getStatus(String responseJson) {
        LotStatusResponseDto lotStatusResponseDto = new Gson().fromJson(responseJson, LotStatusResponseDto.class);

        if (lotStatusResponseDto.getStatus().toUpperCase().equals("FINISHED")) {
            lotService.finish(lotStatusResponseDto.getId());
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void showTimer() {
        template.convertAndSend("/topic/timer", new TimerResponseDto(String.valueOf(new Date().getTime())));
    }

}
