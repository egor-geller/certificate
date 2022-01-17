package com.epam.esm.service;

public interface LoginProvider {

    String createToken(String username, String password);
}
