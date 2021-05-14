package ru.koChyan.Auction.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.User;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;

@Component
public class SubscriptionDAO {

    @Autowired
    private EntityManager em;

    @Transactional
    public void removeAllSubscribers(Long lotId) {

        String query = "DELETE FROM subscription " +
                "WHERE subscription.lot_id = :lotId";

        em.createNativeQuery(query)
                .setParameter("lotId", lotId)
                .executeUpdate();
    }

    @Transactional(readOnly = true)
    public List<User> findAllSubscribers(Long lotId) {
        String query = "SELECT user.* FROM user " +
                "WHERE user.id = " +
                "(SELECT subscription.subscriber_id FROM subscription " +
                "WHERE subscription.lot_id = :lotId) " +
                "ORDER BY user.id";

        return em.createNativeQuery(query, User.class)
                .setParameter("lotId", lotId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Page<Lot> findAllLots(Long userId, Pageable pageable) {

        String queryLotPage = "SELECT lot.* FROM lot " +
                "WHERE lot.id IN " +
                "(SELECT subscription.lot_id FROM subscription " +
                "WHERE subscription.subscriber_id = :userId)" +
                "ORDER BY lot.id " +
                "LIMIT :startIndex, :size";

        List<Lot> lots = em.createNativeQuery(queryLotPage, Lot.class)
                .setParameter("userId", userId)
                .setParameter("startIndex", pageable.getPageNumber() * pageable.getPageSize())
                .setParameter("size", pageable.getPageSize())
                .getResultList();

        String queryLotCount = "SELECT COUNT(*) FROM lot " +
                "WHERE lot.id IN " +
                "(SELECT subscription.lot_id FROM subscription " +
                "WHERE subscription.subscriber_id = :userId)";

        BigInteger lotCount = (BigInteger)em.createNativeQuery(queryLotCount)
                .setParameter("userId", userId)
                .getSingleResult();



        Page<Lot> pageLot = new PageImpl<Lot>(lots, pageable, lotCount.longValue());

        return pageLot;
    }

    @Transactional(readOnly = false)
    public void saveSubscription(Long lotId, Long userId) {
        String query = "INSERT INTO auction.subscription (lot_id, subscriber_id) " +
                "VALUES (:lotId, :subscriberId)";

        em.createNativeQuery(query)
                .setParameter("lotId", lotId)
                .setParameter("subscriberId", userId)
                .executeUpdate();
    }

    @Transactional(readOnly = false)
    public void deleteSubscription(Long lotId, Long userId) {
        String query = "DELETE FROM auction.subscription " +
                "WHERE lot_id = :lotId AND subscriber_id = :subscriberId";

        em.createNativeQuery(query)
                .setParameter("lotId", lotId)
                .setParameter("subscriberId", userId)
                .executeUpdate();
    }

}
