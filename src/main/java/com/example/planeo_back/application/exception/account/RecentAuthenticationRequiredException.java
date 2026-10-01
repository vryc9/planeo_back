package com.example.planeo_back.application.exception.account;

public class RecentAuthenticationRequiredException extends RuntimeException {
    public RecentAuthenticationRequiredException() {
        super("Une re-authentification récente est requise pour supprimer le compte");
    }
}
