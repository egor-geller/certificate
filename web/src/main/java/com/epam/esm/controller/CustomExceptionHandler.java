package com.epam.esm.controller;

import com.epam.esm.exception.AttachedTagException;
import com.epam.esm.exception.DataException;
import com.epam.esm.exception.EmptyOrderException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.InvalidJsonException;
import com.epam.esm.exception.PaginationException;
import com.epam.esm.exception.QueryBuildException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.epam.esm.controller.ErrorCode.CODE_ERROR_400;
import static com.epam.esm.controller.ErrorCode.CODE_ERROR_401;
import static com.epam.esm.controller.ErrorCode.CODE_ERROR_404;
import static com.epam.esm.controller.ErrorCode.CODE_ERROR_415;
import static com.epam.esm.controller.ErrorCode.CODE_ERROR_500;
import static com.epam.esm.controller.ErrorMessages.ATTACH_DETACH_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.DELETE_ATTACHED_TAG_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.EMPTY_ORDER_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.ENTITY_ALREADY_EXISTS_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.ENTITY_NOT_FOUND_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.ERROR_CODE;
import static com.epam.esm.controller.ErrorMessages.ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.INTERNAL_SERVER_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.INVALID_CREDENTIALS_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.INVALID_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.NOT_JSON_FORMAT_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.PAGINATION_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.PROBLEM_BUILDING_QUERY_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.REQUEST_NOT_VALID_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.REQUEST_PARAMETER_NOT_VALID_ERROR_MESSAGE;
import static com.epam.esm.controller.ErrorMessages.RESOURCE_NOT_FOUND_MESSAGE;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public CustomExceptionHandler(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        String errorMessage = getErrorMessage(RESOURCE_NOT_FOUND_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, errorMessage, CODE_ERROR_404);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        String errorMessage = getErrorMessage(NOT_JSON_FORMAT_ERROR_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, errorMessage, CODE_ERROR_415);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {
        String errorMessage = getErrorMessage(REQUEST_PARAMETER_NOT_VALID_ERROR_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_415);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = getErrorMessage(REQUEST_NOT_VALID_ERROR_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> entityAlreadyExistsHandle() {
        String errorMessage = getErrorMessage(ENTITY_ALREADY_EXISTS_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundExceptionHandle(EntityNotFoundException e) {
        String errorMessage = String.format(getErrorMessage(ENTITY_NOT_FOUND_MESSAGE), e.getEntityId());
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<Object> invalidEntityExceptionHandle(InvalidEntityException e) {
        String errorMessage = String.format(getErrorMessage(INVALID_MESSAGE), e.getCauseEntity());
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<Object> dataExceptionHandle(DataException e) {
        String errorMessage = String.format(getErrorMessage(ATTACH_DETACH_ERROR_MESSAGE), e.getTagId(), e.getCertificateId());
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(QueryBuildException.class)
    public ResponseEntity<Object> queryBuildExceptionHandle(QueryBuildException e) {
        String errorMessage = String.format(getErrorMessage(PROBLEM_BUILDING_QUERY_ERROR_MESSAGE), e.getSortBy());
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(AttachedTagException.class)
    public ResponseEntity<Object> attachedTagExceptionHandle(AttachedTagException e) {
        String tag = e.getTagId() + ", " + e.getTagName();
        String errorMessage = String.format(getErrorMessage(DELETE_ATTACHED_TAG_ERROR_MESSAGE), tag);
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(InvalidJsonException.class)
    public ResponseEntity<Object> invalidJsonExceptionHandle() {
        String errorMessage = getErrorMessage(NOT_JSON_FORMAT_ERROR_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE, errorMessage, CODE_ERROR_415);
    }

    @ExceptionHandler(EmptyOrderException.class)
    public ResponseEntity<Object> emptyOrderExceptionHandle() {
        String errorMessage = getErrorMessage(EMPTY_ORDER_ERROR_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, CODE_ERROR_500);
    }

    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<Object> paginationExceptionHandle(PaginationException e) {
        String errorMessage = String.format(getErrorMessage(PAGINATION_ERROR_MESSAGE),
                e.getErrorType(), e.getInvalidValue());
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, errorMessage, CODE_ERROR_400);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsExceptionHandle() {
        String errorMessage = getErrorMessage(INVALID_CREDENTIALS_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.UNAUTHORIZED, errorMessage, CODE_ERROR_401);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultHandle() {
        String errorMessage = getErrorMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        return buildErrorResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, CODE_ERROR_500);
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
