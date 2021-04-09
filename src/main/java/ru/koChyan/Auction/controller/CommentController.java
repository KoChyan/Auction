package ru.koChyan.Auction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.koChyan.Auction.domain.Comment;
import ru.koChyan.Auction.domain.Lot;
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


        model.addAttribute("comments", commentService.getAll());
        return "comment/lotCommentList";
    }

    @PostMapping()
    public String addComment(
            @PathVariable Lot lot,
            Model model,
            @Valid Comment comment,
            BindingResult bindingResult,
            @RequestParam(name = "text") String text
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, List<String>> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);
            model.addAttribute("comment", comment);
            return "comment/lotCommentList";
        } else {

            return "redirect:/lot/" +lot.getId() + "/comment";
        }
    }

}