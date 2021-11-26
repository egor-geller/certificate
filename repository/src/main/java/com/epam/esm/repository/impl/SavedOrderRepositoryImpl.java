package com.epam.esm.repository.impl;

import com.epam.esm.entity.SavedOrder;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.CreateRepository;
import com.epam.esm.repository.repositoryinterfaces.SavedOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.SavedOrderQueries.SELECT_ALL_SAVED_ORDERS;
import static com.epam.esm.repository.query.SavedOrderQueries.SELECT_BY_ORDER_ID;

@Repository
public class SavedOrderRepositoryImpl implements SavedOrderRepository, CreateRepository<SavedOrder> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SavedOrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<SavedOrder> findAll(PaginationContext paginationContext) {
        return entityManager.createQuery(SELECT_ALL_SAVED_ORDERS, SavedOrder.class)
                .setFirstResult(paginationContext.getStartPage())
                .setMaxResults(paginationContext.getLengthOfContext())
                .getResultList();
    }

    @Override
    public Optional<SavedOrder> findById(Long id) {
        return Optional.ofNullable(entityManager.find(SavedOrder.class, id));
    }

    @Transactional
    @Override
    public SavedOrder create(SavedOrder savedOrder) {
        entityManager.persist(savedOrder);
        entityManager.flush();
        return savedOrder;
    }

    @Override
    public Long count() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        query.select(criteriaBuilder.count(query.from(SavedOrder.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<SavedOrder> findByOrderId(Long id) {
        return entityManager.createQuery(SELECT_BY_ORDER_ID, SavedOrder.class)
                .setParameter(1, id)
                .getResultList();
    }

    @Override
    public List<SavedOrder> findByUserId(Long id) {
        return entityManager.createQuery("SELECT u FROM SavedOrder u WHERE u.order.user.id = ?1", SavedOrder.class)
                .setParameter(1, id)
                .getResultList();
    }
}
