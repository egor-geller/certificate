package com.epam.esm.service;

import com.epam.esm.dto.AuthenticateDto;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.InvalidEntityException;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthenticationService {

    /**
     * Create a new user.
     *
     * @param userDto {@link UserDto} instance
     * @throws InvalidEntityException in case when passed DTO object contains invalid data
     * @throws EntityAlreadyExistsException in case when user with specified username already exists
     * @return {@link TokenDto} object
     */
    AuthenticateDto signup(UserDto userDto);

    /**
     * Authenticate with provided credentials.
     *
     * @param authenticateDto {@link AuthenticateDto} instance
     * @throws BadCredentialsException in case when provided credentials are wrong
     * @return {@link TokenDto} object
     */
    AuthenticateDto login(AuthenticateDto authenticateDto);
}
