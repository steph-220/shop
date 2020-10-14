package com.company.shop.model;

import java.util.List;

public class ErrorResponse {

    private String errorMessage;
    private Integer errorCode;
    private List<String> validationErrors;

    private ErrorResponse() {
    }

    public static ErrorResponse create(String errorMessage, Integer errorCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.errorMessage = errorMessage;
        errorResponse.errorCode = errorCode;

        return errorResponse;
    }

    public static ErrorResponse create(List<String> validationErrors, Integer errorCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.validationErrors = validationErrors;
        errorResponse.errorCode = errorCode;

        return errorResponse;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
