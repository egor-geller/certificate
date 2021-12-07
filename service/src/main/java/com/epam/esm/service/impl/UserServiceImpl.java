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
import org.apache.commons.lang3.StringUtils;
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
        User user = userServiceMapper.convertUserFromDto(userDto);

        List<ValidationError> validationErrors = userValidator.validate(user);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(User.class);
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        Optional<User> userById = userRepository.findById(savedUser.getId());
        if (userById.isEmpty()) {
            throw new EntityNotFoundException(String.valueOf(savedUser.getId()));
        }
        return userServiceMapper.convertUserToDto(userById.get());
    }

    @Override
    public UserDto login(UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User " + username + " not found"));

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
