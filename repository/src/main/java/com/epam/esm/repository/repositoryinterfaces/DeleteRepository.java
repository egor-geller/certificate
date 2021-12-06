package com.epam.esm.repository.repositoryinterfaces;


public interface DeleteRepository<T> {

    /**
     * Delete a tag
     *
     * @param t an entity
     */
    void delete(T t);
}
