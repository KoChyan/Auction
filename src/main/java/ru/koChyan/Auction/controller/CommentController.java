package ru.koChyan.Auction.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.CommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lot/{lot}/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping()
    public String commentList(
            @PathVariable Lot lot,
            Model model
    ) {

        model.addAttribute("lot", lot);
        model.addAttribute("comments", commentService.getAllByLotId(lot.getId()));
        return "comment/lotCommentList";
    }

    @PostMapping()
    public String addComment(
            @AuthenticationPrincipal User user,
            @PathVariable Lot lot,
            Model model,
            @Valid Comment comment,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("lot", lot);
            model.addAttribute("comments", commentService.getAllByLotId(lot.getId()));
            return "comment/lotCommentList";
        } else {

            commentService.addComment(comment, lot, user);
            return "redirect:/lot/" + lot.getId() + "/comment";
        }
    }

    @PostMapping("/{comment}/delete")
    public String deleteComment(
            @PathVariable Lot lot,
            @PathVariable Comment comment
    ) {

        commentService.remove(comment);
        return "redirect:/lot/" + lot.getId() + "/comment";
    }

}