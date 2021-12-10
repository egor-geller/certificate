package com.epam.esm.security;

import com.epam.esm.controller.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.epam.esm.controller.ErrorMessages.ERROR_CODE;
import static com.epam.esm.controller.ErrorMessages.ERROR_MESSAGE;

@Component
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String UNAUTHORIZED_MESSAGE = "unauthorized";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private final ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public AppAuthenticationEntryPoint(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String errorMessage = getErrorMessage();
        ResponseEntity<Object> objectResponseEntity = buildErrorResponseEntity(errorMessage);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(objectResponseEntity));
    }

    private String getErrorMessage() {
        Locale locale = LocaleContextHolder.getLocale();
        return bundleMessageSource.getMessage(UNAUTHORIZED_MESSAGE, null, locale);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(String errorMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put(ERROR_MESSAGE, errorMessage);
        body.put(ERROR_CODE, ErrorCode.CODE_ERROR_401);

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}
