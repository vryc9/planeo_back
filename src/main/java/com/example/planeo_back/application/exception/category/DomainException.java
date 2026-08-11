package com.example.planeo_back.application.exception.category;


public sealed class DomainException extends RuntimeException
        permits InvalidCategoryException {
    public DomainException(String message) {
        super(message);
    }
}
