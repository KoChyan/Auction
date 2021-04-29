package ru.koChyan.Auction.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.koChyan.Auction.domain.Lot;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotRepo extends JpaRepository<Lot, Long> {

    List<Lot> findByName(String name);

    Page<Lot> findAllByStatus(String status, Pageable pageable);

    Optional<Lot> findById(Long id);
}
