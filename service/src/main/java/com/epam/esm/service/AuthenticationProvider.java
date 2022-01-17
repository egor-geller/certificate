package com.epam.esm.service;

public interface AuthenticationProvider {

    void authenticateUser(String authorizationHeader);
}
