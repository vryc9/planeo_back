package com.example.planeo_back.web.controller;

import com.example.planeo_back.application.service.balance.BalanceService;
import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.web.DTO.BalanceResponseDTO;
import com.example.planeo_back.web.DTO.balance.BalanceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

    @GetMapping("{id}")
    public ResponseEntity<BalanceResponseDTO> getBalanceById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<BalanceResponseDTO> createBalance(@RequestBody BalanceDTO balanceDTO) throws IllegalAccessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(balanceDTO));
    }

    @GetMapping("/exist")
    public ResponseEntity<Boolean> balanceExistForUser() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.balanceExistForUser());
    }

    @PutMapping
    public ResponseEntity<BalanceResponseDTO> update(@RequestBody BigDecimal amount) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(amount));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBalance(@RequestBody BalanceDTO balanceDTO) {
        service.delete(balanceDTO);
        return ResponseEntity.ok().build();
    }
}
