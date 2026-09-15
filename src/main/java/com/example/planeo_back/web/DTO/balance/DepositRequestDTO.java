package com.example.planeo_back.web.DTO.balance;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositRequestDTO(
        @NotNull(message = "Le montant du dépôt est obligatoire")
        BigDecimal amount,

        @NotNull(message = "Le compte destinataire du dépôt est obligatoire")
        Long accountId
) {
}
