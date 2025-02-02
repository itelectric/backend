package com.itelectric.backend.v1.domain.exception;

public class ConflictException extends Exception {
    public ConflictException(String message) {
        super(message);
    }
}
