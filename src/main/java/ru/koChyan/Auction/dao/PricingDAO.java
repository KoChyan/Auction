package ru.koChyan.Auction.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.koChyan.Auction.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class PricingDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    public User findWinner(Long lotId) {

        String query = "SELECT user.* " +
                "FROM user JOIN pricing " +
                "ON user.id = (" +
                "SELECT pricing.user_id FROM pricing " +
                "WHERE pricing.lot_id = :lotId " +
                "ORDER BY pricing.date DESC LIMIT 1" +
                ") " +
                "WHERE user.balance >= pricing.bet " +
                "ORDER BY pricing.bet DESC " +
                "LIMIT 1";

        return (User)em.createNativeQuery(query, User.class)
                .setParameter("lotId", lotId)
                .getResultStream().findFirst().orElse(null);
    }

}
