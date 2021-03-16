package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Pricing;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.PricingRepo;

import java.util.Date;
import java.util.List;

@Service
public class PricingService {

    private final PricingRepo pricingRepo;

    @Autowired
    public PricingService(PricingRepo pricingRepo) {
        this.pricingRepo = pricingRepo;
    }

    public void addPrice(User user, Lot lot, Date date){
        Pricing pricing = new Pricing();

        pricing.setUser(user);
        pricing.setDate(date);
        pricing.setLot(lot);
        pricing.setBet(lot.getFinalRate());

        pricingRepo.save(pricing);
    }

    public List<Pricing> findLastThreeByLotId(Long id) {
        return pricingRepo.findLastThreeByLotId(id);
    }
}
