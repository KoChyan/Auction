package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.dao.PricingDAO;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.repos.PricingRepo;

import java.util.Date;

@Service
public class BetService {

    @Autowired
    private PricingDAO pricingDAO;

    @Autowired
    private PricingRepo pricingRepo;

    public Long getTimeLeft(Lot lot){
        long timeNow = new Date().getTime();
        long timeLastBet = pricingDAO.getLastPricingDateByLotId(lot.getId()).getTime();

        //перевод мин в мс
        long timeStep = lot.getTimeStep()*60000;

        return timeLastBet + timeStep - timeNow;
    }
}
