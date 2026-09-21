package com.example.planeo_back.web.DTO;

import java.math.BigDecimal;

public record BalanceResponseDTO(
     BigDecimal currentBalance,
     BigDecimal futureBalance,
     BigDecimal pendingExpense
    ) {
}

