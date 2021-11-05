package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.UserQueries.FIND_ALL_USERS;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final PaginationContext paginationContext;

    @Autowired
    public UserRepositoryImpl(EntityManager entityManager, PaginationContext paginationContext) {
        this.paginationContext = paginationContext;
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(FIND_ALL_USERS, User.class)
                .setFirstResult(paginationContext.getStartPage())
                .setMaxResults(paginationContext.getLengthOfContext())
                .getResultList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }
}
