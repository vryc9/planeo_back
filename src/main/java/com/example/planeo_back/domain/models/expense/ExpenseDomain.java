package com.example.planeo_back.domain.models.expense;

import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.category.CategoryDomain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDomain(
        Long id,
        String username,
        BigDecimal amount,
        String label,
        CategoryDomain category,
        ExpenseStatus status,
        Boolean recurring,
        LocalDate date
) {
    public ExpenseDomain markAsProcessed() {
        return new ExpenseDomain(id, username, amount, label, category,  ExpenseStatus.PROCESSED, recurring, date);
    }

    public boolean isProcessed() {
        return status == ExpenseStatus.PROCESSED;
    }
}
