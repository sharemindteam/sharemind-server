package com.example.sharemind.global.exception;

import com.example.sharemind.auth.exception.AuthException;
import com.example.sharemind.customer.exception.CustomerException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<CustomExceptionResponse> catchAuthException(AuthException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<CustomExceptionResponse> catchCustomerException(CustomerException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<CustomExceptionResponse> catchGlobalException(GlobalException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CustomExceptionResponse.of(e.getErrorCode().name(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionResponse> catchMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomExceptionResponse.of(HttpStatus.BAD_REQUEST.name(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomExceptionResponse> catchConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomExceptionResponse.of(HttpStatus.BAD_REQUEST.name(), e.getConstraintViolations().stream().findFirst().get().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionResponse> catchException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }
}