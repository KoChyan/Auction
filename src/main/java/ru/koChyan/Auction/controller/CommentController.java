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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.koChyan.Auction.controller.util.ControllerUtils;
import ru.koChyan.Auction.domain.Comment;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Status;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.CommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lot/{lot}/comment")
public class CommentController {

    //default size
    private final static int PAGE_SIZE = 5;

    @Autowired
    private CommentService commentService;


    @GetMapping()
    public String commentList(
            @PathVariable Lot lot,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (lot.getStatus().equals(Status.ACTIVE.name())) { // если лот все еще активен
            model.addAttribute("lot", lot);
            model.addAttribute("url", "/lot/" + lot.getId() + "/comment");
            model.addAttribute("page", commentService.getAllByLotId(lot.getId(), pageable));
            return "comment/lotCommentList";
        } else {
            return "redirect:/lot";
        }
    }

    @PostMapping()
    public String addComment(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot,
            Model model,
            @Valid Comment comment,
            BindingResult bindingResult,
            @PageableDefault(size = PAGE_SIZE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable

    ) {
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("lot", lot);
            model.addAttribute("url", "/lot/" + lot.getId() + "/comment");
            model.addAttribute("page", commentService.getAllByLotId(lot.getId(), pageable));
            return "comment/lotCommentList";
        } else {
            if (lot.getStatus().equals(Status.ACTIVE.name())) { //если лот все еще активен

                commentService.addComment(comment, lot, user);
                return "redirect:/lot/" + lot.getId() + "/comment";
            } else {
                return "redirect:/lot";
            }
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')") // требуем права админа для удаления комментария
    @PostMapping("/{comment}/delete")
    public String deleteComment(
            @PathVariable Lot lot,
            @PathVariable Comment comment
    ) {
        if (lot.getStatus().equals(Status.ACTIVE.name())) { //если лот все еще активен
            commentService.remove(comment);
            return "redirect:/lot/" + lot.getId() + "/comment";
        } else {
            return "redirect:/lot";
        }
    }

}