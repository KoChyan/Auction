package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.dao.PricingDAO;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Pricing;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.PricingRepo;

import java.util.Date;
import java.util.List;

@Service
public class PricingService {

    @Autowired
    private PricingRepo pricingRepo;

    @Autowired
    private PricingDAO pricingDAO;

    public void addPrice(User user, Lot lot){
        Pricing pricing = new Pricing();

        pricing.setUser(user);
        pricing.setDate(lot.getStartTime());
        pricing.setLot(lot);
        pricing.setBet(lot.getInitialBet());

        pricingRepo.save(pricing);
    }

    public List<Pricing> findLastThreeByLotId(Long id) {
        return pricingRepo.findLastThreeByLotId(id);
    }

    public void addPrice(User user, Lot lot, Long bet, Date date) {
        Pricing pricing = new Pricing();

        pricing.setUser(user);
        pricing.setDate(date);
        pricing.setLot(lot);
        pricing.setBet(bet);

        pricingRepo.save(pricing);
    }

    public Date getLastPricingDateByLotId(Long id){
        return pricingDAO.getLastPricingDateByLotId(id);
    }

}
