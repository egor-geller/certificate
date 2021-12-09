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

    private static final Logger logger = LogManager.getLogger();

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
        logger.info("UserRepoImpl - username: {}", username);
        User user = new User();
        try {
            user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = ?1", User.class)
                    .setParameter(1, username)
                    .getSingleResult();
        }catch (PersistenceException | IllegalArgumentException e) {
            logger.warn("Exception in sql: {}", e.getMessage());
        }
        logger.info("UserRepoImpl - User: {}", user);
        return Optional.ofNullable(user);
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}
