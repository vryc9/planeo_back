package com.example.planeo_back.domain.service;

import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculateFutureBalance {

    public static BigDecimal calculFutureBalance(List<ExpenseDomain> expenses, BalanceDomain balance, BigDecimal amount) {

        if (expenses.isEmpty()) {
            return balance.currentBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP);
        }

        if (balance.futureBalance().compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal totalExpenses = expenses.stream()
                    .map(ExpenseDomain::amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return balance.currentBalance().subtract(totalExpenses).setScale(2, RoundingMode.HALF_UP);
        }

        return balance.futureBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP);
    }
}