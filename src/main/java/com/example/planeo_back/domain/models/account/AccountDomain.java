package com.example.planeo_back.domain.models.account;

import com.example.planeo_back.application.exception.account.AccountMessage;
import com.example.planeo_back.application.exception.account.InvalidAccountException;

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

    public AccountDomain withdraw(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAccountException(AccountMessage.INVALID_PARAMETER);
        }
        if (amount.compareTo(montant) < 0) {
            throw new InvalidAccountException(AccountMessage.INSUFFICIENT_BALANCE);
        }
        return withAmount(amount.subtract(montant));
    }

    public AccountDomain credit(BigDecimal montant) {
        return withAmount(amount.add(montant));
    }

    public AccountDomain withAmount(BigDecimal newAmount) {
        return new AccountDomain(id, username, label, newAmount, logo);
    }
}
