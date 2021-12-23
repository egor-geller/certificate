package com.epam.esm.dto;

import java.util.Objects;

public class ErrorDto {

    private String errorMessage;
    private String errorCode;

    public ErrorDto(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDto errorDto = (ErrorDto) o;
        return Objects.equals(errorMessage, errorDto.errorMessage) && Objects.equals(errorCode, errorDto.errorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, errorCode);
    }

    @Override
    public String toString() {
        return "ErrorDto{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
