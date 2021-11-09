package com.epam.esm.service;

import com.epam.esm.repository.PaginationContext;

/**
 * Generic interface for Entity service.
 *
 * @param <T> generic type parameter
 */
public interface EntityService<T> {

    /**
     * Generic creation of an entity
     *
     * @param t generic param
     * @return generic param
     */
    T create(PaginationContext paginationContext, T t);

    /**
     * Generic update of an entity
     *
     * @param t generic param
     * @return generic param
     */
    T update(T t);

    /**
     * Delete an entity.
     *
     * @param id the id
     * @return {@code True} if delete was successful, otherwise {@code False}
     */
    boolean delete(Long id, PaginationContext paginationContext);
}
