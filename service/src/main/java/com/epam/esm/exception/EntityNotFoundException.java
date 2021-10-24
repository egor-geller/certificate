package com.epam.esm.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityNotFoundException extends RuntimeException {

    private Long[] id;

    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(Long... id) {
        this.id = id;
    }

    public EntityNotFoundException(String tagName) {
        super(tagName);
    }

    public List<Long> getEntityId() {
        return Arrays.stream(id).collect(Collectors.toList());
    }
}
