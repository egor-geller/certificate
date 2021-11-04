package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * The base interface for all repositories.
 *
 * @param <T> type of Entity
 */
public interface Repository<T> {

    /**
     * Fina a tag or certificate by its unique Id
     *
     * @param id id of a tag or certificate
     * @return {@link Optional} of {@link Tag}, {@link Certificate}, {@link Order} or {@link User} entity.
     */
    Optional<T> findById(Long id);

    /**
     * Create a new certificate or tag.
     *
     * @param t {@link Certificate}, {@link Tag} or {@link Order} instance
     * @return entity of {@link Certificate}, {@link Tag} or {@link Order}
     */
    T create(T t);

    /**
     * Delete a tag
     *
     * @param t entity of {@link Tag} or {@link Certificate}
     */
    void delete(T t);
}
