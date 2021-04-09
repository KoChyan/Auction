package ru.koChyan.Auction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.domain.Comment;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.service.CommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping()
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/lot/{lot}/comment")
    public String commentList(
            @PathVariable Lot lot,
            Model model
    ) {

        model.addAttribute("comments", commentService.getAll());
        return "comment/lotCommentList";
    }

    @PostMapping("/lot/{lot}/comment")
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
            model.addAttribute("comments", commentService.getAll());
            return "comment/lotCommentList";
        } else {

            commentService.addComment(comment, lot, user);
            return "redirect:/lot/" +lot.getId() + "/comment";
        }
    }

    @PostMapping("/comment/delete/{comment}")
    public String deleteComment(
            @PathVariable Comment comment

    ){


        return "redirect:/lot/" + "/comment";
    }

}