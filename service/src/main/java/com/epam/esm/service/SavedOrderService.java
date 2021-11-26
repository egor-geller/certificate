package com.epam.esm.service;

import com.epam.esm.dto.SavedOrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.SavedOrder;
import com.epam.esm.repository.PaginationContext;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link SavedOrder} entity.
 *
 * @author Geller Egor
 */
public interface SavedOrderService {

    /**
     * Finds all saved orders
     *
     * @param paginationContext page context
     * @return list of {@link SavedOrderDto} entity.
     */
    List<SavedOrderDto> findAll(PaginationContext paginationContext);

    /**
     * Find a saved order by its id
     *
     * @param id id of a saved order
     * @return {@link UserDto} entity.
     */
    SavedOrderDto findById(Long id);

    List<SavedOrderDto> findByOrderId(Long id);
    List<SavedOrderDto> findByUserId(Long id);

    /**
     * Create saved order.
     *
     * @param savedOrderDto {@link SavedOrderDto} instance
     * @return {@link SavedOrderDto} object that represents saved order
     */
    SavedOrderDto create(SavedOrderDto savedOrderDto);

    /**
     * Count all saved orders.
     *
     * @return {@code Long} sum of all records
     */
    Long count();
}
