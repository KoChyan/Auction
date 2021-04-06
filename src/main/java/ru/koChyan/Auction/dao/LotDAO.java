package ru.koChyan.Auction.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.koChyan.Auction.domain.Lot;

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
    public void updateLastBet(Long lotId, Long bet){

        em.createNativeQuery("UPDATE lot SET lot.final_bet = :bet WHERE lot.id = :id")
                .setParameter("bet", bet)
                .setParameter("id", lotId)
                .executeUpdate();
    }

    public List<Lot> findByFilter(String filterName, String filterDescription) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Lot> criteria = cb.createQuery(Lot.class);

        Root<Lot> root = criteria.from(Lot.class);
        Predicate predicate = cb.conjunction();

        if(filterName != null && !filterName.trim().isEmpty()){
            predicate = cb.and(cb.equal(root.get("name"), filterName));
        }
        if(filterDescription != null && !filterDescription.trim().isEmpty()){
            predicate.getExpressions().add(cb.like(root.get("description"), filterDescription));
        }

        criteria.where(predicate);
        return em.createQuery(criteria).getResultList();
    }
}
