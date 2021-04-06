package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.PricingService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lot")
public class LotController {
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private LotService lotService;

    @Autowired
    private PricingService pricingService;

    @GetMapping
    public String listLots(
            Model model,
            @RequestParam(name = "name", required = false, defaultValue = "") String filterByName,
            @RequestParam(name = "description", required = false, defaultValue = "") String filterByDescription
    ) {


        model.addAttribute("lots", lotService.findByFilter(filterByName, filterByDescription));
        return "lot/lotList";
    }


    @GetMapping("/add")
    public String lotForm(Model model) {

        return "lot/lotAdd";
    }

    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Lot lot,
            BindingResult bindingResult,
            Model model,
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException {

        //если есть ошибки при вводе данных
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("lot", lot);
            return "lot/lotAdd";
        }

        lotService.addLot(user, lot, file);
        return "redirect:/lot";
    }

    @GetMapping("/{lot}/bet")
    public String listBets(
            @PathVariable Lot lot,
            Model model
    ) {

        model.addAttribute("lot", lot);
        model.addAttribute("pricing", pricingService.findLastThreeByLotId(lot.getId()));

        return "lot/lotBets";
    }

    @PostMapping("/{lot}/bet")
    public String addBet(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot,
            @RequestParam(name = "bet") Long bet,
            @RequestParam(name = "date") String date,
            Model model
    ) {

        pricingService.addPrice(user, lot, bet, new Date(date));
        lotService.updateLastBet(lot, bet);

        model.addAttribute("lot", lot);
        model.addAttribute("pricing", pricingService.findLastThreeByLotId(lot.getId()));

        return "lot/lotBets";
    }

    @GetMapping("/{lot}/comment")
    public String listComments(
            @PathVariable Lot lot,
            Model model
    ) {

        return "lot/lotComments";
    }

}
