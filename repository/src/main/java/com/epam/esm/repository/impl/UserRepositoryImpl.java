package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.UserQueries.FIND_ALL_USERS;
import static com.epam.esm.repository.query.UserQueries.FIND_BY_USERNAME;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll(PaginationContext paginationContext) {
        return entityManager.createQuery(FIND_ALL_USERS, User.class)
                .setFirstResult(paginationContext.getStartPage())
                .setMaxResults(paginationContext.getLengthOfContext())
                .getResultList();

    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public Long count() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        query.select(criteriaBuilder.count(query.from(User.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user;
        try {
            user = entityManager.createQuery(FIND_BY_USERNAME, User.class)
                    .setParameter(1, username)
                    .getSingleResult();
        } catch (PersistenceException | IllegalArgumentException e) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}
