package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.CreateRepository;
import com.epam.esm.repository.repositoryinterfaces.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.OrderQueries.SELECT_ALL_ORDERS;
import static com.epam.esm.repository.query.OrderQueries.SELECT_ORDER_BY_USER_ID;

@Repository
public class OrderRepositoryImpl implements OrderRepository, CreateRepository<Order> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Order> findAll(PaginationContext paginationContext) {
        return entityManager.createQuery(SELECT_ALL_ORDERS, Order.class)
                .setFirstResult(paginationContext.getStartPage())
                .setMaxResults(paginationContext.getLengthOfContext())
                .getResultList();
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public Long count() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        query.select(criteriaBuilder.count(query.from(Order.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Order> findByUserId(PaginationContext paginationContext, Long id) {
        return entityManager.createQuery(SELECT_ORDER_BY_USER_ID, Order.class)
                .setParameter(1, id)
                .getResultList();
    }

    @Override
    public Long countByUser(Long id) {
        return entityManager.createQuery(SELECT_ORDER_BY_USER_ID, Order.class)
                .setParameter(1, id)
                .getResultStream()
                .count();
    }

    @Transactional
    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        entityManager.flush();
        return order;
    }
}
