package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * Interface that provides functionality for manipulating {@link User} entity.
 *
 * @author Geller Egor
 */
public interface UserRepository extends Repository<User> {

    Optional<User> findByUsername(String username);

    User save(User user);
}
