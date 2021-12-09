package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserServiceMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.UserValidator;
import com.epam.esm.validator.ValidationError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private final UserRepository userRepository;
    private final UserServiceMapper userServiceMapper;
    private final PasswordEncoder encoder;
    private final UserValidator userValidator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserServiceMapper userServiceMapper,
                           PasswordEncoder encoder,
                           UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userServiceMapper = userServiceMapper;
        this.encoder = encoder;
        this.userValidator = userValidator;
    }

    @Override
    public List<UserDto> findAllUsers(PaginationContext paginationContext) {
        return userRepository.findAll(paginationContext)
                .stream()
                .map(userServiceMapper::convertUserToDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserDto findUserByIdService(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return userServiceMapper.convertUserToDto(user);
    }

    @Transactional
    @Override
    public UserDto signup(UserDto userDto) {
        logger.info("UserServiceImpl - userDto: {}", userDto);
        List<ValidationError> validationErrors = userValidator.validate(userDto);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(User.class);
        }

        Optional<User> maybeSignuped = userRepository.findByUsername(userDto.getUsername());

        if (maybeSignuped.isEmpty()) {
            logger.info("UserServiceImpl - maybeSignuped: {}", maybeSignuped);
            throw new EntityAlreadyExistsException();
        }

        String encodedPassword = encoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        User user = userServiceMapper.convertUserFromDto(userDto);
        logger.info("UserServiceImpl - user: {}", user);
        User savedUser = userRepository.save(user);

        return userServiceMapper.convertUserToDto(savedUser);
    }

    @Override
    public UserDto login(UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User " + username + " not found"));
        logger.info("UserServiceImpl - user: {}", user);
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Login/Password is not correct");
        }

        return userDto;
    }

    @Override
    public Long count() {
        return userRepository.count();
    }
}
