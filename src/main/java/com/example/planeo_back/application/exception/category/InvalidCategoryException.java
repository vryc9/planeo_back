package com.example.planeo_back.application.exception.category;


public final class InvalidCategoryException extends DomainException {
    public InvalidCategoryException(CategoryMessage message) {
        super(message.getLabel());
    }
}

