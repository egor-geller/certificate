package com.epam.esm.repository.repositoryinterfaces;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.PaginationContext;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link Order} entity.
 *
 * @author Geller Egor
 */
public interface OrderRepository extends Repository<Order> {

    /**
     * Find an order by its user id
     *
     * @param id of the a user
     * @return {@link List} of {@link Order} entity.
     */
    List<Order> findByUserId(PaginationContext paginationContext, Long id);

    Long countByUser();
}
