package com.example.planeo_back.domain.models;

import java.math.BigDecimal;

public record ExpensePerMonthDomain(int month, BigDecimal amount) {
    public static ExpensePerMonthDomain build(int month, BigDecimal amount) {
        return new ExpensePerMonthDomain(month, amount);
    }
}
