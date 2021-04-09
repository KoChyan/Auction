package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.domain.Comment;
import ru.koChyan.Auction.repos.CommentRepo;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;


    public List<Comment> getAll(){
        return commentRepo.findAll();
    }

    public List<Comment> getAllByAuthorId(Long id){
        return commentRepo.findAllByAuthorId(id);
    }
}
