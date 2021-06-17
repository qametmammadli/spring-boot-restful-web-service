package com.qamedev.restful.exception;

public enum ErrorMessages {

    AUTHENTICATION_FAILED("Authentication failed"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    EMAIL_NOT_VERIFIED("Email could not be verified"),
    TOKEN_EXPIRED("Token has been expired"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record with provided data is not found"),
    REGISTERED_EMAIL("This Email already exists"),
    REQUIRED_FIELD("Required field. Field should not be empty");

    private final String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
