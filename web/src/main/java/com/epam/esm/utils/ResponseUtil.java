package com.epam.esm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class ResponseUtil {

    private static final String ERROR_MESSAGE = "errorMessage";

    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public ResponseUtil(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getErrorMessage(String errorMessageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorMessageName, null, locale);
    }

    public Map<String, Object> buildErrorResponseMap(String errorMessage) {
        Map<String, Object> map = new HashMap<>();
        map.put(ERROR_MESSAGE, errorMessage);

        return map;
    }

    public ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String errorMessage) {
        Map<String, Object> body = buildErrorResponseMap(errorMessage);

        return new ResponseEntity<>(body, status);
    }
}
