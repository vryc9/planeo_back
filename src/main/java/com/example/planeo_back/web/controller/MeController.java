package com.example.planeo_back.web.controller;

import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.application.usecase.accountdeletion.RequestAccountDeletionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class MeController {

    private final RequestAccountDeletionUseCase requestAccountDeletion;
    private final AuthService authService;

    public MeController(RequestAccountDeletionUseCase requestAccountDeletion, AuthService authService) {
        this.requestAccountDeletion = requestAccountDeletion;
        this.authService = authService;
    }

    /** Needs a recent password re-confirmation (POST /auth/reauth on the gateway), else 403. */
    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount() {
        requestAccountDeletion.execute(authService.getUsername(), authService.getReauthenticatedAt());
        return ResponseEntity.noContent().build();
    }
}
