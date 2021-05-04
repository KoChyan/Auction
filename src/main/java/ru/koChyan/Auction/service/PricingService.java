package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.dao.PricingDAO;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Pricing;
import ru.koChyan.Auction.domain.User;
import ru.koChyan.Auction.repos.PricingRepo;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PricingService {

    @Autowired
    private PricingRepo pricingRepo;

    @Autowired
    private PricingDAO pricingDAO;


    public void addPrice(User user, Lot lot) {
        Pricing pricing = new Pricing();

        pricing.setUser(user);
        pricing.setDate(lot.getStartTime());
        pricing.setLot(lot);
        pricing.setBet(lot.getInitialBet());

        pricingRepo.save(pricing);
    }

    public void addPrice(User user, Lot lot, Long bet, Date date) {
        Pricing pricing = new Pricing();

        pricing.setUser(user);
        pricing.setDate(date);
        pricing.setLot(lot);
        pricing.setBet(bet);

        pricingRepo.save(pricing);
    }

    public Pricing getLastByLotId(Long id) {
        return pricingRepo.findFirstByLotIdOrderByDateDesc(id);
    }

    public User getWinner(Long lotId){
        return pricingDAO.findWinner(lotId);
    }

    public String getTimerText(int xHoursBefore, Lot lot) {

        if (isMoreThanXHoursBefore(xHoursBefore, lot)) { // если до начала торгов более xHours часов
            return "Дата начала аукциона: ";

        } else if (isLessThanXHoursBefore(xHoursBefore, lot)) { // если до начала торгов менее xHours часов
            return "До начала аукциона: ";

        } else {                            // если торги уже идут
            return "До конца аукциона: ";
        }
    }

    public String getTimerValue(int xHoursBefore, Lot lot) {

        if (isMoreThanXHoursBefore(xHoursBefore, lot)) { // если до начала торгов более xHours часов
            return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(lot.getStartTime()); // то выводим дату начала аукциона

        } else if (isLessThanXHoursBefore(xHoursBefore, lot)) { // если до начала торгов менее xHours часов

            // время до начала торгов в секундах
            long timeLeft = (lot.getStartTime().getTime() - new Date().getTime()) / 1000;

            long secondsLeft = timeLeft % 60; // 1..59
            long minutesLeft = (timeLeft - secondsLeft) / 60 % 60; // 1..59
            long hoursLeft = (timeLeft - minutesLeft * 60 - secondsLeft) / 3600 % 60; // 1..23

            return hoursLeft + " ч, " + minutesLeft + " м, " + secondsLeft + " с"; // то выводим время до старта аукциона

        } else {              //если торги уже идут
            Pricing pricing = pricingRepo.findFirstByLotIdOrderByDateDesc(lot.getId());
            long intervalMillis = lot.getTimeStep() * 60000; // 1 мин = 60 000 мс
            long timeLeft = (pricing.getDate().getTime() + intervalMillis - new Date().getTime()) / 1000;

            return String.valueOf(timeLeft);
        }
    }

    public boolean isMoreThanXHoursBefore(int xHoursBefore, Lot lot) {
        long xHoursInMillis = xHoursBefore * 3600000;  // 1 час = 3 600 000 мс
        long timeLeft = lot.getStartTime().getTime() - new Date().getTime(); // время начала торгов - время сейчас

        return timeLeft > xHoursInMillis; //до начала торгов более xHours часов
    }

    public boolean isLessThanXHoursBefore(int xHoursBefore, Lot lot) {
        long xHoursInMillis = xHoursBefore * 3600000;  // 1 час = 3 600 000 мс
        long timeLeft = lot.getStartTime().getTime() - new Date().getTime(); // время начала торгов - время сейчас

        return timeLeft < xHoursInMillis && timeLeft > 0; // торги еще не начались && до начала менее xHours часов
    }

}
