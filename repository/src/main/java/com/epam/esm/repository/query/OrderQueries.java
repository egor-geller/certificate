package com.epam.esm.repository.query;

public final class OrderQueries {

    public static final String SELECT_ALL_ORDERS = "SELECT o FROM Order o";
    public static final String SELECT_ORDER_BY_USER_ID = "SELECT o FROM Order o WHERE o.user= ?1";

    private OrderQueries() {
    }
}
