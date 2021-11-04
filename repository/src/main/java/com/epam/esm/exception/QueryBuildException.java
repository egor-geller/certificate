package com.epam.esm.exception;

public class QueryBuildException extends RuntimeException{

    private final String sortBy;

    public QueryBuildException(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortBy() {
        return sortBy;
    }
}
