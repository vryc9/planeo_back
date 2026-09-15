package com.example.planeo_back.domain.models.balance;

import java.math.BigDecimal;

public record BalanceDomain (
        Long id,
        String username,
        BigDecimal currentBalance,
        BigDecimal pendingExpense
        ){
}
