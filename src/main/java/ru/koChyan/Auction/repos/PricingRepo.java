package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.koChyan.Auction.domain.Pricing;

@Repository
public interface PricingRepo extends JpaRepository<Pricing, Long> {

    Pricing findFirstByLotIdOrderByDateDesc(Long id);
}
