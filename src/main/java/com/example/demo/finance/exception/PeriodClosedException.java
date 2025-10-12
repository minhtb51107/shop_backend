package com.example.demo.finance.exception;

public class PeriodClosedException extends RuntimeException {
    public PeriodClosedException(String message) {
        super(message);
    }
}