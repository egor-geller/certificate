package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.repository.PaginationContext;

import java.util.List;

/**
 * Interface that provides functionality for manipulating {@link User} entity.
 *
 * @author Geller Egor
 */
public interface UserService {

    /**
     * Finds all stored users
     *
     * @param paginationContext page context
     * @return list of {@link UserDto} entity.
     */
    List<UserDto> findAllUsers(PaginationContext paginationContext);

    /**
     * Fina a user by its unique Id
     *
     * @param id id of a user
     * @return {@link UserDto} entity.
     */
    UserDto findUserByIdService(Long id);

    Long count();
}
