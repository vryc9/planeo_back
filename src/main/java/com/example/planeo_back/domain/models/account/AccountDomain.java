package com.example.planeo_back.domain.models.account;

import java.math.BigDecimal;

public record AccountDomain(
        Long id,
        String username,
        String label,
        BigDecimal amount
) {
    public static AccountDomain build(String username, String label, BigDecimal amount) {
        return new AccountDomain(null, username, label, amount);
    }

    public AccountDomain withAmount(BigDecimal newAmount) {
        return new AccountDomain(id, username, label, newAmount);
    }
}
