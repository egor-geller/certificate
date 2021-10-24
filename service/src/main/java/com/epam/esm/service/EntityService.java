package com.epam.esm.service;

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
    T create(T t);

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
    boolean delete(Long id);
}
