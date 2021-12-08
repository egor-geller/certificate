package com.epam.esm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SIGNUP_ENDPOINT = "/api/v1/users/signup";
    private static final String LOGIN_ENDPOINT = "/api/v1/users/login";
    private static final String USER_ENDPOINT = "/api/v1/users";
    private static final String TAGS_ENDPOINT = "/api/v1/tags/**";
    private static final String MAIN_ENTITY_ENDPOINT = "/api/v1/certificates/**";
    private static final String GENERATE_ENDPOINT = "/api/v1/generatedata/**";

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SIGNUP_ENDPOINT, LOGIN_ENDPOINT).permitAll()
                .antMatchers(HttpMethod.GET, USER_ENDPOINT).hasRole(ADMIN_ROLE)
                .antMatchers(MAIN_ENTITY_ENDPOINT).hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .antMatchers(TAGS_ENDPOINT).hasRole(ADMIN_ROLE)
                .antMatchers(GENERATE_ENDPOINT).hasRole(ADMIN_ROLE)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
