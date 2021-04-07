package ru.koChyan.Auction.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void updateProfile(Long userId, String newPassword, String newEmail) {

        em.createQuery("UPDATE User u SET " +
                "u.password = :newPassword, " +
                "u.email = :newEmail " +
                "WHERE u.id = :id")
                .setParameter("newPassword", newPassword)
                .setParameter("newEmail", newEmail)
                .setParameter("id", userId)
                .executeUpdate();
    }

}
