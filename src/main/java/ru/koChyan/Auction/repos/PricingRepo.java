package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.koChyan.Auction.domain.Pricing;

import java.util.List;

public interface PricingRepo extends JpaRepository<Pricing, Long> {

    @Query(value = "SELECT * FROM pricing WHERE pricing.lot_id = ? ORDER BY date DESC LIMIT 0, 3", nativeQuery = true )
    List<Pricing> findLastThreeByLotId(Long id);

    List<Pricing> findAllByLotId(Long id);
}
