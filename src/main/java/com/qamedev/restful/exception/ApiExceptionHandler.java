package com.qamedev.restful.exception;

import com.qamedev.restful.ui.response.ErrorResponsePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = UserServiceException.class)
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, HttpServletRequest request){
        ErrorResponsePayload responsePayload = new ErrorResponsePayload(LocalDateTime.now(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(responsePayload, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleAllOtherExceptions(Exception ex, HttpServletRequest request){
        ErrorResponsePayload responsePayload = new ErrorResponsePayload(LocalDateTime.now(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(responsePayload, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
