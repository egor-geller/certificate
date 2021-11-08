package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.repository.PaginationContext;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link Order} entity.
 *
 * @author Geller Egor
 */
public interface OrderService {

    /**
     * Finds all stored users
     *
     * @param paginationContext page context
     * @return list of {@link UserDto} entity.
     */
    List<OrderDto> findAllOrdersService(PaginationContext paginationContext);

    /**
     * Find a user by its unique Id
     *
     * @param id id of a user
     * @return {@link UserDto} entity.
     */
    OrderDto findOrderByIdService(Long id);

    /**
     * Finds all orders of specified user.
     *
     * @param userId user id
     * @param paginationContext {@link PaginationContext} object with pagination logic
     * @return list of {@link OrderDto}
     */
    List<OrderDto> findByOrderByUserService(long userId, PaginationContext paginationContext);

    /**
     * Make an order.
     *
     * @param orderDto {@link OrderDto} instance (only {@code userId} and {@code certificateId} are required)
     * @return {@link OrderDto} object that represents created order
     */
    OrderDto makeOrderService(OrderDto orderDto);
}
