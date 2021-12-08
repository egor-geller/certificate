package com.epam.esm.validator;

import com.epam.esm.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.epam.esm.validator.ValidationError.INVALID_PASSWORD;
import static com.epam.esm.validator.ValidationError.INVALID_USERNAME;

@Component
public class UserValidator {

    private static final String USERNAME_REGEX = "^[\\p{LD}_]{8,32}$";
    private static final String PASSWORD_REGEX = "^(?=.*\\p{Alpha})(?=.*\\d)[\\p{Alnum}]{8,32}$";

    public List<ValidationError> validate(UserDto userDto) {
        List<ValidationError> validationErrors = new ArrayList<>();
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        boolean usernameIsValid = validateCredentials(username, USERNAME_REGEX);
        if (!usernameIsValid) {
            validationErrors.add(INVALID_USERNAME);
        }

        boolean passwordIsValid = validateCredentials(password, PASSWORD_REGEX);
        if (!passwordIsValid) {
            validationErrors.add(INVALID_PASSWORD);
        }

        return validationErrors;
    }

    private boolean validateCredentials(String field, String rgx) {
        return Pattern.matches(rgx, field);
    }
}
