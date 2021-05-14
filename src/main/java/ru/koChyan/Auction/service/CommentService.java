package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.domain.Comment;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.CommentRepo;

import java.util.Date;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;


    public void addComment(Comment comment, Lot lot, User author){
        comment.setAuthor(author);
        comment.setLot(lot);
        comment.setDate(new Date());

        commentRepo.save(comment);
    }

    public void remove(Comment comment){
        commentRepo.delete(comment);
    }

    public Page<Comment> getAllByLotId(Long id, Pageable pageable) {
        return commentRepo.findByLotIdOrderByDateDesc(id, pageable);
    }
}
