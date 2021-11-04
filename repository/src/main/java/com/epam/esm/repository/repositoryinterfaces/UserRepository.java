package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.User;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link User} entity.
 *
 * @author Geller Egor
 */
public interface UserRepository extends Repository<User> {

    /**
     * Finds all stored orders
     *
     * @return list of {@link User} entity.
     */
    List<User> findAll();
}
