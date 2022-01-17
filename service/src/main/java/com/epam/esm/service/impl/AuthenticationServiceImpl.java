package com.epam.esm.service.impl;

import com.epam.esm.dto.AuthenticateDto;
import com.epam.esm.dto.TokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserServiceMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.repository.repositoryinterfaces.UserRepository;
import com.epam.esm.service.AuthenticationService;
import com.epam.esm.validator.UserValidator;
import com.epam.esm.validator.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserValidator userValidator;
    private final UserServiceMapper userServiceMapper;
    private final ProviderService providerService;


    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder encoder,
                                     UserValidator userValidator,
                                     UserServiceMapper userServiceMapper,
                                     ProviderService providerService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userValidator = userValidator;
        this.userServiceMapper = userServiceMapper;
        this.providerService = providerService;
    }

    @Transactional
    @Override
    public AuthenticateDto signup(UserDto userDto) {
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
        User signUpUser = userRepository.save(user);
        return new AuthenticateDto(signUpUser.getUsername(), signUpUser.getPassword());
    }

    @Override
    public TokenDto login(AuthenticateDto authenticateDto) {
        String username = authenticateDto.getUsername();
        String password = authenticateDto.getPassword();
        String accessToken = providerService.createToken(username, password);
        return new TokenDto(accessToken);
    }
}
