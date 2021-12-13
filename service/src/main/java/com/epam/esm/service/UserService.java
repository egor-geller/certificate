package com.epam.esm.service;

import com.epam.esm.dto.AuthenticateDto;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import org.springframework.security.authentication.BadCredentialsException;

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

    /**
     * Create a new user.
     *
     * @param userDto {@link UserDto} instance
     * @throws InvalidEntityException in case when passed DTO object contains invalid data
     * @throws EntityAlreadyExistsException in case when user with specified username already exists
     * @return {@link TokenDto} object
     */
    TokenDto signup(UserDto userDto);

    /**
     * Authenticate with provided credentials.
     *
     * @param authenticateDto {@link AuthenticateDto} instance
     * @throws BadCredentialsException in case when provided credentials are wrong
     * @return {@link TokenDto} object
     */
    AuthenticateDto login(AuthenticateDto authenticateDto);

    Long count();
}
