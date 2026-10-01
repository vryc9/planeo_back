package com.example.planeo_back.infrastructure.kafka;

public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
