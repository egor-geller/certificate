package com.epam.esm.validator;

import com.epam.esm.exception.InvalidEntityException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class TagValidator {

    private TagValidator() {
    }

    private static final String NAME_REGEX = "[a-zA-Z0-9.,'?!]{2,30}";
    private static final String VALID_ID_REGEX = "\\^\\d+";

    public void validateTagValid(String tagName) {
        if (tagName == null || tagName.isEmpty() || !Pattern.matches(NAME_REGEX, tagName)) {
            throw new InvalidEntityException(TagValidator.class);
        }
    }

    public void validateId(Long... id) {
        if (id == null || id.length < 1) {
            throw new InvalidEntityException(TagValidator.class);
        }
        Arrays.stream(id).forEach(i -> {
            String valueOfId = String.valueOf(i);
            if (!valueOfId.matches(VALID_ID_REGEX) && i < 1) {
                throw new InvalidEntityException(TagValidator.class);
            }
        });
    }
}
