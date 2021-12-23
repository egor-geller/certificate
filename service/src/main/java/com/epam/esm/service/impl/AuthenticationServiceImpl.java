package com.epam.esm.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${secret.key}")
    private String secretKey;
    @Value("${claim.role}")
    private String role;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserValidator userValidator;
    private final UserServiceMapper userServiceMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder encoder,
                                     UserValidator userValidator,
                                     UserServiceMapper userServiceMapper,
                                     AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userValidator = userValidator;
        this.userServiceMapper = userServiceMapper;
        this.authenticationManager = authenticationManager;
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

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        org.springframework.security.core.userdetails.User userDetail =
                (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String accessToken = JWT.create()
                .withSubject(userDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withClaim(role, userDetail.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
        return new TokenDto(accessToken);
    }
}
