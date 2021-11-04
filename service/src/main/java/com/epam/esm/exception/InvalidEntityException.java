package com.epam.esm.exception;

public class InvalidEntityException extends RuntimeException {

    private final Class<?> causeEntity;

    public InvalidEntityException(Class<?> classCause) {
        this.causeEntity = classCause;
    }

    public Class<?> getCauseEntity() {
        return causeEntity;
    }
}
