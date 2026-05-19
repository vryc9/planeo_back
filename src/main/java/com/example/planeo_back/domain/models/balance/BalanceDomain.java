package com.example.planeo_back.domain.models.balance;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record BalanceDomain (
        Long id,
        String username,
        BigDecimal currentBalance,
        BigDecimal futureBalance,
        BigDecimal pendingExpense
        ){

    public BalanceDomain withFutureBalance(BigDecimal pendingSum) {
        return new BalanceDomain(
                id,
                username,
                currentBalance,
                currentBalance.subtract(pendingSum).setScale(2, RoundingMode.HALF_UP),
                pendingExpense
        );
    }

}
