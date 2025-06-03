package com.microservicos.ProductServiceAPI.errors;

public class ErrorModelView {
    private String message;

    public ErrorModelView(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 