package com.itelectric.backend.v1.domain.exception;

public class ForbiddenException extends Exception {
    public ForbiddenException(String message) {
        super(message);
    }
}
