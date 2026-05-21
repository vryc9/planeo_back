package com.example.planeo_back.domain.models.expense;

import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.enums.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDomain(
        Long id,
        String username,
        BigDecimal amount,
        String label,
        Tag tag,
        ExpenseStatus status,
        Boolean recurring,
        LocalDate date
) {
    public ExpenseDomain markAsProcessed() {
        return new ExpenseDomain(id, username, amount, label, tag,  ExpenseStatus.PROCESSED, recurring, date);
    }

    public boolean isProcessed() {
        return status == ExpenseStatus.PROCESSED;
    }
}
