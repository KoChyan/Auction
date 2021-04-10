package ru.koChyan.Auction.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.koChyan.Auction.domain.Pricing;

import java.util.List;

@Repository
public interface PricingRepo extends JpaRepository<Pricing, Long> {

    @Query(value = "SELECT * FROM pricing WHERE pricing.lot_id = ? ORDER BY bet DESC LIMIT 3", nativeQuery = true )
    List<Pricing> findLastThreeByLotId(Long id);

    List<Pricing> findAllByLotId(Long id);

}
