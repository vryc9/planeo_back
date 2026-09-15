package com.example.planeo_back.domain.models.account;

import java.math.BigDecimal;

public record AccountDomain(
        Long id,
        String username,
        String label,
        BigDecimal amount,
        String logo
) {
    public static AccountDomain build(String username, String label, BigDecimal amount, String logo) {
        return new AccountDomain(null, username, label, amount, logo);
    }

    public AccountDomain withAmount(BigDecimal newAmount) {
        return new AccountDomain(id, username, label, newAmount, logo);
    }
}
