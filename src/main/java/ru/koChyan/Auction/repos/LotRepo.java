package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koChyan.Auction.domain.Lot;

import java.util.List;

public interface LotRepo extends JpaRepository<Lot, Long> {
    List<Lot> findByName(String name);
}
