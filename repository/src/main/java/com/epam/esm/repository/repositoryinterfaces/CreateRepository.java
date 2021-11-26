package com.epam.esm.repository.repositoryinterfaces;

public interface CreateRepository<T> {

    /**
     * Create a new entity
     *
     * @param t an entity instance
     * @return entity
     */
    T create(T t);
}
