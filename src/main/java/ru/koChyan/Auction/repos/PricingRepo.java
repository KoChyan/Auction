package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.koChyan.Auction.domain.Pricing;

public interface PricingRepo extends JpaRepository<Pricing, Long> {

}
