package com.itelectric.backend.config;

import com.itelectric.backend.api.v1.dto.Response;
import com.itelectric.backend.domain.exception.BusinessException;
import com.itelectric.backend.domain.exception.ConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    //Dealing with customized exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        Response response = new Response(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response> handleBusinessExceptions(Exception ex) {
        Response response = new Response(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Response> handleConflictExceptions(Exception ex) {
        Response response = new Response(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleOtherExceptions(Exception ex) {
        log.error(ex.getMessage(), ex);
        Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "An unexpected error occurred. Please try again later.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
