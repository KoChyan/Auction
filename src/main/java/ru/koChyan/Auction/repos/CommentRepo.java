package ru.koChyan.Auction.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.koChyan.Auction.domain.Comment;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    Page<Comment> findByLotIdOrderByDateDesc(Long id, Pageable pageable);
}
