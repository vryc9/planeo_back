package com.example.planeo_back.web.DTO.account;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BalanceTransfertDTO(
        @NotNull(message = "L'id de la banque d'origine doit être présent")
        Long accountOriginId,

        @NotNull(message = "l'id de la banque destinataire est obligatoire")
        Long accountTargetId,

        @NotNull(message = "Le montant doit être présent")
        @Min(value = 1, message = "Le montant du transfert doit être supérieur à 1€")
        BigDecimal amount) {
}
