package com.epam.esm.exception;

import com.epam.esm.PaginationErrorType;

public class PaginationException extends RuntimeException{

    private final PaginationErrorType paginationErrorType;
    private final int invalidValue;

    public PaginationException(PaginationErrorType paginationErrorType, int invalidValue) {
        this.paginationErrorType = paginationErrorType;
        this.invalidValue = invalidValue;
    }

    public PaginationErrorType getErrorType() {
        return paginationErrorType;
    }

    public int getInvalidValue() {
        return invalidValue;
    }
}
