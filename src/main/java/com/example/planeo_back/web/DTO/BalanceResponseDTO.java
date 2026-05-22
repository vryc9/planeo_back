package com.example.planeo_back.web.DTO;

import com.example.planeo_back.domain.models.balance.BalanceDomain;

import java.math.BigDecimal;

public record BalanceResponseDTO(
     Long id,
     BigDecimal currentBalance,
     BigDecimal futureBalance,
     BigDecimal pendingExpense
    ) {

    public static BalanceResponseDTO from(BalanceDomain domain) {
        return new BalanceResponseDTO(
                domain.id(),
                domain.currentBalance(),
                domain.futureBalance(),
                domain.pendingExpense()
        );
    }


}

