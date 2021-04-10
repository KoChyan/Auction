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

        //если аукцион уже начался
        if(lot.getStartTime().before(new Date())){

            long timeNow = new Date().getTime();
            long timeLastBet = pricingDAO.getLastPricingDateByLotId(lot.getId()).getTime();

            //перевод мин в мс
            long timeStep = lot.getTimeStep()*60000;

            //время до конца аукциона, секунд
            return  (timeLastBet + timeStep - timeNow) / 1000;
        }

        //если аукцион еще не начался
        return null;
    }

    public Long getTimeBefore(Lot lot){

        //если аукцион еще не начался
        if(lot.getStartTime().after(new Date())){

            long startTime = lot.getStartTime().getTime();
            long timeNow = new Date().getTime();

            //время до начала аукциона, секунд
            return (startTime - timeNow) / 1000;
        }

        //если время начала аукциона уже прошло
        return null;
    }
}
