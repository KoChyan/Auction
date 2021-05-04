package ru.koChyan.Auction.dao;

import com.google.common.base.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.koChyan.Auction.domain.Lot;
import ru.koChyan.Auction.domain.Status;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Component
public class LotDAO {

    @PersistenceContext
    private EntityManager em;


    public Page<Lot> findByFilter(String filterName, String filterDescription, Pageable pageable) {
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

        // выводить только лоты со статусом "ACTIVE"
        predicate.getExpressions().add(cb.equal(root.get("status"), Status.ACTIVE.name()));

        criteria.orderBy(cb.asc(root.get("startTime"))); // сортировка по дате, по возрастанию

        criteria.where(predicate);

        TypedQuery<Lot> query = em.createQuery(criteria);
        int totalRows = query.getResultList().size(); // ра

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<Lot>(query.getResultList(), pageable, totalRows);
    }

    @Transactional
    public void setStatus(Long id, String status) {

        if (!Strings.isNullOrEmpty(status)) {

            String query = "UPDATE lot SET lot.status = :status " +
                    "WHERE lot.id = :id";

            em.createNativeQuery(query)
                    .setParameter("status", status)
                    .setParameter("id", id)
                    .executeUpdate();
        }
    }

    @Transactional
    public void updateStatus() {
        String query = "UPDATE lot SET lot .status = :newStatus " +
                "WHERE lot.end_time <= :nowDate " +
                "AND lot.status = :oldStatus";

        em.createNativeQuery(query)
                .setParameter("nowDate", new Date())
                .setParameter("newStatus", Status.FINISHED.name())
                .setParameter("oldStatus", Status.ACTIVE.name())
                .executeUpdate();
    }

    @Transactional
    public List<BigInteger> getLotIdToBeUpdated(){

        String query = "SELECT lot.id FROM lot " +
                "WHERE lot.end_time <= :nowDate " +
                "AND lot.status = :oldStatus";

        return em.createNativeQuery(query)
                .setParameter("nowDate", new Date())
                .setParameter("oldStatus", Status.ACTIVE.name())
                .getResultList();
    }
}
