package ru.koChyan.Auction.dao;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Component
public class PricingDAO {

    @PersistenceContext
    private EntityManager em;

    public Date getLastPricingDateByLotId(Long id){

        return (Date)em.createNativeQuery("SELECT date FROM pricing " +
                "WHERE lot_id = :lotId " +
                "ORDER BY bet DESC LIMIT 1")
                .setParameter("lotId", id)
                .getSingleResult();

    }


}
