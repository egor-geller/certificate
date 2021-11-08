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
     * Fina all entities
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
     * Fina a tag or certificate by its unique Id
     *
     * @param id id of a tan entity
     * @return {@link Optional} of an entity
     */
    Optional<T> findById(Long id);
}
