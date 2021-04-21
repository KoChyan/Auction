package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.koChyan.Auction.domain.Lot;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotRepo extends JpaRepository<Lot, Long> {
    List<Lot> findByName(String name);
    List<Lot> findAllByStatus(String status);
    Optional<Lot> findById(Long id);
}
