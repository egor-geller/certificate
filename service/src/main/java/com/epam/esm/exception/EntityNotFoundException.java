package com.epam.esm.exception;

public class EntityNotFoundException extends RuntimeException {

    private Long[] id;

    public EntityNotFoundException(Long... id) {
        this.id = id;
    }

    public EntityNotFoundException(String tagName) {
        super(tagName);
    }

    public Long[] getEntityId() {
        return id;
    }
}
