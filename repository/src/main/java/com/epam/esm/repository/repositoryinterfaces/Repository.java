package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.SearchCriteria;

import java.util.List;
import java.util.Optional;

/**
 * The base interface for all repositories.
 *
 * @param <T> type of Entity
 */
public interface Repository<T> {

    /**
     * Find all entities
     *
     * @return {@link List} of entities
     */
    default List<T> findAll(PaginationContext paginationContext) {
        return List.of();
    }

    default List<T> find(PaginationContext paginationContext, SearchCriteria searchCriteria) {
        return List.of();
    }

    /**
     * Find a tag or certificate by its unique Id
     *
     * @param id id of a tan entity
     * @return {@link Optional} of an entity
     */
    Optional<T> findById(Long id);

    /**
     * Retrieves the total size of all objects from database
     *
     * @return {@code Long} total number of entities
     */
    Long count();
}
