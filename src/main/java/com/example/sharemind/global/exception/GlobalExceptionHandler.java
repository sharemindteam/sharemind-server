package com.example.sharemind.global.exception;

import com.example.sharemind.auth.exception.AuthException;
import com.example.sharemind.customer.exception.CustomerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<GlobalExceptionResponse> catchAuthException(AuthException e) {
        log.error(e.getErrorCode().getErrorMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getErrorHttpStatus())
                .body(GlobalExceptionResponse.of(e.getErrorCode().getErrorHttpStatus(), e.getErrorCode().getErrorMessage()));
    }

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<GlobalExceptionResponse> catchCustomerException(CustomerException e) {
        log.error(e.getErrorCode().getErrorMessage(), e);
        return ResponseEntity.status(e.getErrorCode().getErrorHttpStatus())
                .body(GlobalExceptionResponse.of(e.getErrorCode().getErrorHttpStatus(), e.getErrorCode().getErrorMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalExceptionResponse> catchMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalExceptionResponse.of(HttpStatus.BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionResponse> catchException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(GlobalExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
