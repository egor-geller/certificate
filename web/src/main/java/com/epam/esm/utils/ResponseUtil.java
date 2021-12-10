package com.epam.esm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class ResponseUtil {

    private static final String ERROR_MESSAGE = "errorMessage";

    private final ResourceBundleMessageSource messageSource;
    private final Map<String, Object> map = new HashMap<>();

    @Autowired
    public ResponseUtil(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getErrorMessage(String errorMessageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorMessageName, null, locale);
    }

    public Map<String, Object> buildErrorResponseMap(String errorMessage) {
        map.put(ERROR_MESSAGE, errorMessage);
        return map;
    }
}
