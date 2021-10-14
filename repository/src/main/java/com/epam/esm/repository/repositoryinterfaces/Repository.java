package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

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
     * @return {@link Optional} of {@link Tag} or {@link Certificate} entity.
     */
    Optional<T> findById(Long id);

    /**
     * Create a new certificate or tag.
     *
     * @param t {@link Certificate} or {@link Tag} instance
     */
    void create(T t);

    /**
     * Delete a tag
     *
     * @param id id of the tag or certificate
     * @return {@code true} if {@link Tag} or {@link Certificate} was deleted, otherwise {@code false}
     */
    boolean delete(Long id);
}
