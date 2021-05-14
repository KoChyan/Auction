package ru.koChyan.Auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.domain.dto.LotDto;
import ru.koChyan.Auction.domain.dto.MessageDto;
import ru.koChyan.Auction.service.LotService;
import ru.koChyan.Auction.service.SubscriptionService;
import ru.koChyan.Auction.validator.LotValidator;
import ru.koChyan.Auction.validator.MessageDtoValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lot")
public class LotController {

    //default size
    private final static int PAGE_SIZE = 6;

    @Autowired
    private LotService lotService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private LotValidator lotValidator;

    @Autowired
    private MessageDtoValidator messageDtoValidator;

    @InitBinder("lotDto")
    protected void initLotBinder(WebDataBinder binder) {
        binder.setValidator(lotValidator);
    }

    @InitBinder("messageDto")
    protected void initMessageBinder(WebDataBinder binder) {
        binder.setValidator(messageDtoValidator);
    }

    @GetMapping()
    public String lotList(
            Model model,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "description", required = false, defaultValue = "") String description,
            @PageableDefault(size = PAGE_SIZE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        lotService.finishIfTimeOver(); // обновить статус у уже завершивших свой срок действия лотов

        model.addAttribute("url", "/lot"); // url для построения ссылок для пагинации
        model.addAttribute("page", lotService.getAllActiveByFilter(name, description, pageable));
        return "lot/lotList";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/add")
    public String getLotAddForm() {

        return "lot/addLot";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/add")
    public String addLot(
            @AuthenticationPrincipal User user,
            @Valid LotDto lotDto,
            BindingResult bindingResult,
            Model model,
            @RequestParam(name = "file") MultipartFile file
    ) {
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

    @PreAuthorize("hasAuthority('ADMIN')") // требуем права администратора для формы отмены лота
    @GetMapping("/{lotId}/cancel")
    public String getCancelForm(
            @PathVariable("lotId") Lot lot,
            Model model
    ) {

        model.addAttribute("lot", lot);
        return "lot/cancelLot";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{lotId}/cancel")
    public String cancelLot(
            @PathVariable("lotId") Lot lot,
            Model model,
            @Valid MessageDto reason,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.addAttribute("reason", reason);
            model.mergeAttributes(errorsMap);
            model.addAttribute("lot", lot);
            return "lot/cancelLot";
        } else {

            lotService.cancelLot(lot, reason.getText());
            return "redirect:/lot";
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{lot}/subscribe")
    public String subscribe(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot
    ) {

        subscriptionService.addSubscription(lot.getId(), user.getId());
        return "redirect:/lot";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{lot}/unsubscribe")
    public String unsubscribe(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot
    ) {

        subscriptionService.removeSubscription(lot.getId(), user.getId());
        return "redirect:/lot";
    }
}
