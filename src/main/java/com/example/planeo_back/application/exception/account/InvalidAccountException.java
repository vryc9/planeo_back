package com.example.planeo_back.application.exception.account;

public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException(AccountMessage message) {
        super(message.getLabel());
    }
}
