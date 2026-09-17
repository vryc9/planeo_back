package com.example.planeo_back.web.controller;

import com.example.planeo_back.application.service.account.AccountService;
import com.example.planeo_back.web.DTO.account.AccountCreateRequestDTO;
import com.example.planeo_back.web.DTO.account.AccountDTO;
import com.example.planeo_back.web.DTO.account.BalanceTransfertDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<List<AccountDTO>> create(@Valid @RequestBody List<@Valid AccountCreateRequestDTO> accounts) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(accounts));
    }

    @PostMapping("/transfert")
    public ResponseEntity<Void> transfert(@Valid @RequestBody BalanceTransfertDTO transfertDTO) {
        service.transfert(transfertDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        return ResponseEntity.ok(service.getAccounts());
    }

    @GetMapping("/exist")
    public ResponseEntity<Boolean> accountsExistForUser() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.accountsExistForUser());
    }
}
