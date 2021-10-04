package com.epam.esm.validator;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParametersValidator {

    private ParametersValidator(){}

    private static final String PARAM_LENGTH_REGEX = "\\w{1,30}";

    public static boolean isParamsValid(String... params) {

        return Arrays.stream(params).allMatch(param -> {
            Pattern pattern = Pattern.compile(PARAM_LENGTH_REGEX, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(param);

            return matcher.find();
        });
    }
}
