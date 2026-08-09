package com.example.planeo_back.domain.service;

import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculateFutureBalance {

    public static BigDecimal calculFutureBalance(List<ExpenseDomain> pendingExpenses, BalanceDomain balance, BigDecimal newExpenseAmount) {
        BigDecimal totalPendingExpenses = pendingExpenses.stream()
                .map(ExpenseDomain::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return balance.currentBalance()
                .subtract(totalPendingExpenses)
                .subtract(newExpenseAmount)
                .setScale(2, RoundingMode.HALF_UP);
    }
}