package com.epam.esm.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class TagValidator {

    private TagValidator() {
    }

    private static final String NAME_REGEX = "[a-zA-Z0-9.,'?!\"]{5,30}";

    public boolean isTagValid(String tagName) {
        return !tagName.isEmpty() && Pattern.matches(NAME_REGEX, tagName);
    }
}
