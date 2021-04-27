package ru.koChyan.Auction.dao;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class UserDAO {

    @PersistenceContext
    private EntityManager em;

}
