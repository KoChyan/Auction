package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.PricingService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lot")
public class LotController {

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
    public String getLotForm(Model model) {

        return "lot/addLot";
    }

    @PostMapping("/add")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "startTime") String startTime,
            @Valid Lot lot,
            BindingResult bindingResult,
            Model model,
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException {

        bindingResult = Validator.justFuture(startTime, bindingResult);

        //если есть ошибки при вводе данных
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("lot", lot);
            return "lot/addLot";
        }

        lotService.addLot(user, lot, file);
        return "redirect:/lot";
    }


}
