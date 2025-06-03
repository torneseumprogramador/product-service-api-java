package com.microservicos.ProductServiceAPI.errors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorModelView> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação.");
        return ResponseEntity.badRequest().body(new ErrorModelView(message));
    }

    @ExceptionHandler(ProductValidationError.class)
    public ResponseEntity<ErrorModelView> handleProductValidationError(ProductValidationError ex) {
        return ResponseEntity.badRequest().body(new ErrorModelView(ex.getMessage()));
    }
} 