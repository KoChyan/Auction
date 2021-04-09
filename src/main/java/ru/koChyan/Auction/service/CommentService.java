package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.domain.Comment;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.CommentRepo;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;


    public void addComment(Comment comment, Lot lot, User user){

        comment.setAuthor(user);
        comment.setLot(lot);
        comment.setDate(new Date());

        commentRepo.save(comment);
    }

    public List<Comment> getAll(){
        return commentRepo.findAll();
    }

    public List<Comment> getAllByAuthorId(Long id){
        return commentRepo.findAllByAuthorId(id);
    }
}
