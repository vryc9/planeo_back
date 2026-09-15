package com.example.planeo_back.web.DTO.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountCreateRequestDTO(
        @NotBlank(message = "Le libellé du compte est obligatoire")
        String label,

        @NotNull(message = "Le montant du compte est obligatoire")
        BigDecimal amount,

        @NotNull(message = "Un logo doit être pour ajouter une banque")
        String logo
) {
}
