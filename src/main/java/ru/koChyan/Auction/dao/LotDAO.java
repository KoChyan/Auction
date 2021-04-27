package ru.koChyan.Auction.dao;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Status;
import ru.koChyan.Auction.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class LotDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void updateLastBet(Long lotId, Long bet) {

        String query = "UPDATE lot SET lot.final_bet = :bet " +
                "WHERE lot.id = :id";

        em.createNativeQuery(query)
                .setParameter("bet", bet)
                .setParameter("id", lotId)
                .executeUpdate();
    }

    public List<Lot> findByFilter(String filterName, String filterDescription) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lot> criteria = cb.createQuery(Lot.class);

        Root<Lot> root = criteria.from(Lot.class);
        Predicate predicate = cb.conjunction();

        if (!Strings.isNullOrEmpty(filterName)) {
            predicate = cb.and(cb.equal(root.get("name"), filterName));
        }
        if (!Strings.isNullOrEmpty(filterDescription)) {
            predicate.getExpressions().add(cb.like(root.get("description"), "%" + filterDescription + "%"));
        }

        //выводить только лоты со статусом "active"
        predicate.getExpressions().add(cb.equal(root.get("status"), "ACTIVE"));

        criteria.where(predicate);
        return em.createQuery(criteria).getResultList();
    }

    public User findUserByLotId(Long lotId) {

        String query = "SELECT User FROM User " +
                "JOIN Lot ON User.id = Lot.creator.id " +
                "WHERE Lot.id = :lotId ";

        return (User) em.createQuery(query)
                .setParameter("lotId", lotId)
                .getSingleResult();
    }

    @Transactional
    public void setStatus(Long id, String status) {

        if(!Strings.isNullOrEmpty(status)){

            String query = "UPDATE lot SET lot.status = :status " +
                    "WHERE lot.id = :id";

            em.createNativeQuery(query)
                    .setParameter("status", status)
                    .setParameter("id", id)
                    .executeUpdate();
        }
    }

    @Transactional
    public void updateStatus(){
        String query = "UPDATE lot SET lot .status = :newStatus " +
                "WHERE lot.end_time <= DATE(NOW()) " +
                "AND lot.status = :oldStatus";

        em.createNativeQuery(query)
                .setParameter("newStatus", Status.FINISHED.name())
                .setParameter("oldStatus", Status.ACTIVE.name())
                .executeUpdate();
    }
}
