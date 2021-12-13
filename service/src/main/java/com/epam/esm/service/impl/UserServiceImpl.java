package com.epam.esm.service.impl;

import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserServiceMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.PaginationContext;
import com.epam.esm.repository.repositoryinterfaces.UserRepository;
import com.epam.esm.security.KeycloakUtil;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.UserValidator;
import com.epam.esm.validator.ValidationError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LogManager.getLogger();

    private final UserRepository userRepository;
    private final UserServiceMapper userServiceMapper;
    private final PasswordEncoder encoder;
    private final UserValidator userValidator;
    private final KeycloakUtil keycloakUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserServiceMapper userServiceMapper,
                           PasswordEncoder encoder,
                           UserValidator userValidator,
                           KeycloakUtil keycloakUtil) {
        this.userRepository = userRepository;
        this.userServiceMapper = userServiceMapper;
        this.encoder = encoder;
        this.userValidator = userValidator;
        this.keycloakUtil = keycloakUtil;
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
    public TokenDto signup(UserDto userDto) {
        logger.info("UserServiceImpl - userDto: {}", userDto);
        List<ValidationError> validationErrors = userValidator.validate(userDto);

        if (!validationErrors.isEmpty()) {
            throw new InvalidEntityException(User.class);
        }

        Optional<User> maybeSignuped = userRepository.findByUsername(userDto.getUsername());

        if (maybeSignuped.isPresent()) {
            throw new EntityAlreadyExistsException();
        }

        String encodedPassword = encoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        User user = userServiceMapper.convertUserFromDto(userDto);
        User savedUser = userRepository.save(user);

        return buildTokenDto(savedUser);
    }

    @Override
    public TokenDto login(UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User " + username + " not found"));
        logger.info("UserServiceImpl - user: {}", user);
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Login/Password is not correct");
        }

        return buildTokenDto(user);
    }

    @Override
    public Long count() {
        return userRepository.count();
    }

    private TokenDto buildTokenDto(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (!keycloakUtil.keycloakUserExists(username)) {
            String keycloakUserId = keycloakUtil.createKeycloakUser(user);
            keycloakUtil.attachRoleToUser(user.getRole(), keycloakUserId);
        } else {
            keycloakUtil.resetPassword(username, password);
        }

        String accessToken = keycloakUtil.obtainAccessToken(username, password);

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(accessToken);

        return tokenDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.get().getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), authorities);
    }
}
