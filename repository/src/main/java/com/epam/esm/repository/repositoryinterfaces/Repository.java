package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The base interface for all repositories.
 *
 * @param <T> type of Entity
 */
public interface Repository<T> {

    /**
     * Fina all entities
     *
     * @return {@link List} of entities
     */
    List<T> findAll();

    /**
     * Fina a tag or certificate by its unique Id
     *
     * @param id id of a tan entity
     * @return {@link Optional} of an entity
     */
    Optional<T> findById(Long id);

    /**
     * Create a new certificate or tag.
     *
     * @param t an entity instance
     * @return entity
     */
    T create(T t);

    /**
     * Delete a tag
     *
     * @param t entity of {@link Tag} or {@link Certificate}
     */
    void delete(T t);
}
