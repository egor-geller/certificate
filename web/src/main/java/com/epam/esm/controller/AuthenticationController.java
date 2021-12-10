package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user.
     * Access is allowed to everyone.
     *
     * @param userDto {@link UserDto} instance
     * @throws InvalidEntityException in case when passed DTO object contains invalid data
     * @throws EntityAlreadyExistsException in case when user with specified username already exists
     * @return JSON {@link ResponseEntity} object that contains {@link UserDto} object
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody UserDto userDto) {
        UserDto signup = userService.signup(userDto);
        return new ResponseEntity<>(signup, CREATED);
    }

    /**
     * Authenticate with provided credentials.
     * Access is allowed to everyone.
     *
     * @param userDto {@link UserDto} instance
     * @throws BadCredentialsException in case when provided credentials are wrong
     * @return JSON {@link ResponseEntity} object that contains {@link UserDto} object
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        UserDto login = userService.login(userDto);
        return new ResponseEntity<>(login, CREATED);
    }
}
