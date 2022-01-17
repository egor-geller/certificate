package com.epam.esm.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.service.AuthenticationProvider;
import com.epam.esm.service.LoginProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
public class ProviderService implements AuthenticationProvider, LoginProvider {

    @Value("${secret.key}")
    private String secretKey;
    @Value("${claim.role}")
    private String userRole;
    private static final String BEARER_KEY = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public ProviderService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticateUser(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_KEY)) {
            try {
                String token = authorizationHeader.substring(BEARER_KEY.length());
                Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim(userRole).asArray(String.class);
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (IllegalArgumentException | JWTVerificationException e) {
                logger.error("Authentication problem: {}", e.getMessage());
            }
        }
    }

    @Override
    public String createToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authenticate.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withClaim(userRole, userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }
}
