package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.controller.util.validator.LotValidator;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.LotDto;
import ru.koChyan.Auction.service.LotService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lot")
public class LotController {

    @Autowired
    private LotService lotService;

    @Autowired
    private LotValidator lotValidator;


    @InitBinder("lotDto")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(lotValidator);
    }

    @GetMapping
    public String lotList(
            Model model,
            @RequestParam(name = "name", required = false, defaultValue = "") String byName,
            @RequestParam(name = "description", required = false, defaultValue = "") String byDescription
    ) {

        lotService.updateStatus(); // обновить статус у уже завершивших свой срок действия лотов
        model.addAttribute("lots", lotService.getAllByFilter(byName, byDescription));
        return "lot/lotList";
    }

    @GetMapping("/add")
    public String getLotForm() {

        return "lot/addLot";
    }

    @PostMapping("/add")
    public String addLot(
            @AuthenticationPrincipal User user,
            @Valid LotDto lotDto,
            BindingResult bindingResult,
            Model model,
            @RequestParam(name = "file") MultipartFile file
    ) {

        //если есть ошибки при вводе данных
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("lot", lotDto);
            return "lot/addLot";
        } else {
            lotService.addLot(user, lotDto, file);
            return "redirect:/lot";
        }
    }


}
