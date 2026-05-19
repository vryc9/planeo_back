package com.example.planeo_back.web.DTO.balance;

import java.math.BigDecimal;

public record BalanceDTO(BigDecimal currentBalance,
                         BigDecimal futureBalance
) {
}
