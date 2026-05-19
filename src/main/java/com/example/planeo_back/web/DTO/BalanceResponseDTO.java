package com.example.planeo_back.web.DTO;

import java.math.BigDecimal;

public record BalanceResponseDTO(
     Long id,
     BigDecimal currentBalance,
     BigDecimal futureBalance,
     BigDecimal pendingExpense
    ) {}

