package com.epam.esm.repository.query;

public final class SavedOrderQueries {

    public static final String SELECT_ALL_SAVED_ORDERS = "SELECT saved FROM SavedOrder saved";
    public static final String SELECT_BY_ORDER_ID = "SELECT o FROM SavedOrder o WHERE o.order.id = ?1";
    public static final String SELECT_BY_USER_ID = "SELECT u FROM SavedOrder u WHERE u.order.user.id = ?1";

    private SavedOrderQueries() {
    }
}
