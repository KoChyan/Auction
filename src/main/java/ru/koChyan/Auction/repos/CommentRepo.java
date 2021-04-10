package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.koChyan.Auction.domain.Comment;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findByAuthorId(Long id);

    List<Comment> findByLotIdOrderByDateDesc(Long id);

}
