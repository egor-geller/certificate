package com.epam.esm.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityNotFoundException extends RuntimeException {

    private final String[] id;

    public EntityNotFoundException(String... id) {
        this.id = id;
    }

    public List<String> getEntityId() {
        return Arrays.stream(id).collect(Collectors.toList());
    }
}
