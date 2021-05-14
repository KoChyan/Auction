package ru.koChyan.Auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.koChyan.Auction.dao.SubscriptionDAO;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionDAO subscriptionDAO;


    public void removeAllSubscribers(Long lotId) {
        subscriptionDAO.removeAllSubscribers(lotId);
    }

    public List<User> getAllSubscribersFor(Long lotId) {
        return subscriptionDAO.findAllSubscribers(lotId);
    }

    public Page<Lot> getAllLotsFor(Long userId, Pageable pageable){
        return subscriptionDAO.findAllLots(userId, pageable);
    }

    public void addSubscription(Long lotId, Long userId){
        subscriptionDAO.saveSubscription(lotId, userId);
    }

    public void removeSubscription(Long lotId, Long userId){
        subscriptionDAO.deleteSubscription(lotId, userId);
    }

}
