package com.microservicos.ProductServiceAPI.errors;

public class ProductValidationError extends RuntimeException {
    public ProductValidationError(String message) {
        super(message);
    }
}