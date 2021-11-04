package com.epam.esm.exception;

import com.epam.esm.ErrorType;

public class InvalidPageContextException extends RuntimeException{

    private final ErrorType errorType;
    private final int invalidValue;

    public InvalidPageContextException(ErrorType errorType, int invalidValue) {
        this.errorType = errorType;
        this.invalidValue = invalidValue;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public int getInvalidValue() {
        return invalidValue;
    }
}
