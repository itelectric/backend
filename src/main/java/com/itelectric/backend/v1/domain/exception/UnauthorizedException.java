package com.itelectric.backend.v1.domain.exception;

public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}
