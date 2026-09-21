package com.example.planeo_back.web.controller;

import com.example.planeo_back.application.service.balance.BalanceService;
import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.web.DTO.BalanceResponseDTO;
import com.example.planeo_back.web.DTO.balance.BalanceDTO;
import com.example.planeo_back.web.DTO.balance.DepositRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService service;
    private final AuthService authService;

    public BalanceController(BalanceService balanceService, AuthService authService) {
        this.service = balanceService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<BalanceResponseDTO> getBalance() {
        String username = authService.getUsername();
        return ResponseEntity.ok(service.getBalance(username));
    }

    @PutMapping
    public ResponseEntity<BalanceResponseDTO> deposit(@Valid @RequestBody DepositRequestDTO depositRequestDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.deposit(depositRequestDTO));
    }

}
