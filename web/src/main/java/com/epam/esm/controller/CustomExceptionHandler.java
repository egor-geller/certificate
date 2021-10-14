package com.epam.esm.controller;

import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String ERROR_CODE = "errorCode";
    private static final String RESOURCE_NOT_FOUND_MESSAGE = "resource_not_found";
    private static final String ENTITY_ALREADY_EXISTS_MESSAGE = "entity_already_exists";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "entity_not_found";
    private static final String INVALID_MESSAGE = "invalid_entity";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal_server_error";

    private final ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public CustomExceptionHandler(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        String errorMessage = getErrorMessage(RESOURCE_NOT_FOUND_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.I_AM_A_TEAPOT, errorMessage, "404");
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> entityAlreadyExistsHandle() {
        String errorMessage = getErrorMessage(ENTITY_ALREADY_EXISTS_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.CONFLICT, errorMessage, "409");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundExceptionHandle() {
        String errorMessage = getErrorMessage(ENTITY_NOT_FOUND_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, errorMessage, "404");
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<Object> invalidEntityExceptionHandle() {
        String errorMessage = getErrorMessage(INVALID_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, "400");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultHandle(Exception e) {
        String errorMessage = getErrorMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, "500");
    }

    private String getErrorMessage(String errorMessageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return bundleMessageSource.getMessage(errorMessageName, null, locale);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String errorMessage, String errorCode) {
        Map<String, Object> body = new HashMap<>();
        body.put(ERROR_MESSAGE, errorMessage);
        body.put(ERROR_CODE, errorCode);

        return new ResponseEntity<>(body, status);
    }
}
