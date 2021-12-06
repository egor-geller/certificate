package com.epam.esm.repository.query;

public final class UserQueries {

    public static final String  FIND_ALL_USERS = "SELECT u FROM User u";
    public static final String FIND_BY_USERNAME = "SELECT u FROM User u WHERE u.username = ?1";

    private UserQueries() {
    }
}
