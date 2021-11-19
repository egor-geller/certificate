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
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.SavedOrderQueries.SELECT_ALL_SAVED_ORDERS;

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
        return Optional.empty();
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
        return null;
    }
}
