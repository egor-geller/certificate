package com.epam.esm.validator;

import com.epam.esm.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class TagValidator {

    private TagValidator() {
    }

    private static final String NAME_REGEX = "[a-zA-Z0-9.,'?!]{2,30}";

    public void isTagValid(String tagName) {
        if (tagName == null || tagName.isEmpty() || !Pattern.matches(NAME_REGEX, tagName)) {
            throw new InvalidEntityException(TagValidator.class);
        }
    }
}
