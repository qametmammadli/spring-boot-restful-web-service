package com.qamedev.restful.exception;

public class UserServiceException extends RuntimeException {
    private static final long serialVersionUID = -6216449974684893921L;

    public UserServiceException(String message) {
        super(message);
    }
}
