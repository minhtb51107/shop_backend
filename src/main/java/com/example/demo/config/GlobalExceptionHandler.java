package com.example.demo.config;

import com.example.demo.finance.exception.AccountInactiveException;
import com.example.demo.finance.exception.PeriodClosedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({PeriodClosedException.class, AccountInactiveException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleBusinessException(RuntimeException ex) {
        Map<String, Object> body = Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}