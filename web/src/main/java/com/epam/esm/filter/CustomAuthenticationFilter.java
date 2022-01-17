package com.epam.esm.filter;

import com.epam.esm.service.impl.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final ProviderService providerService;

    @Autowired
    public CustomAuthenticationFilter(@Lazy ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/login") || request.getServletPath().equals("/api/v1/signup") || request.getServletPath().equals("/api/v1/certificates")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            providerService.authenticateUser(authorizationHeader);
            filterChain.doFilter(request, response);
        }
    }
}
