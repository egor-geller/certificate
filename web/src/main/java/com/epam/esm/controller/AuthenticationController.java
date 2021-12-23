package com.epam.esm.controller;

import com.epam.esm.dto.AuthenticateDto;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Create a new user.
     * Access is allowed to everyone.
     *
     * @param userDto {@link UserDto} instance
     * @return JSON {@link ResponseEntity} object that contains {@link TokenDto} object
     * @throws InvalidEntityException       in case when passed DTO object contains invalid data
     * @throws EntityAlreadyExistsException in case when user with specified username already exists
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthenticateDto> signup(@RequestBody UserDto userDto) {
        AuthenticateDto signup = authenticationService.signup(userDto);
        return new ResponseEntity<>(signup, CREATED);
    }

    /**
     * Authenticate with provided credentials.
     * Access is allowed to everyone.
     *
     * @param authenticateDto {@link AuthenticateDto} instance
     * @return JSON {@link ResponseEntity} object that contains {@link TokenDto} object
     * @throws BadCredentialsException in case when provided credentials are wrong
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AuthenticateDto authenticateDto) {
        TokenDto login = authenticationService.login(authenticateDto);
        return new ResponseEntity<>(login, OK);
    }

}
