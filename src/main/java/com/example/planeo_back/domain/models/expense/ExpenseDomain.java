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
        LocalDate date,
        Long accountId
) {
    public ExpenseDomain markAsProcessed() {
        return new ExpenseDomain(id, username, amount, label, category,  ExpenseStatus.PROCESSED, recurring, date, accountId);
    }

    public static ExpenseDomain build(Long id,
                                      String username,
                                      BigDecimal amount,
                                      String label,
                                      CategoryDomain category,
                                      Boolean recurring,
                                      LocalDate date,
                                      Long accountId) {
        return new ExpenseDomain(id, username, amount, label, category, ExpenseStatus.PENDING, recurring, date, accountId);
    }

    public ExpenseDomain withUpdatedDetails(BigDecimal amount, String label, CategoryDomain category,
                                            Boolean recurring, LocalDate date) {
        return new ExpenseDomain(id, username, amount, label, category, status, recurring, date, accountId);
    }

    public ExpenseDomain reopen() {
        return new ExpenseDomain(id, username, amount, label, category, ExpenseStatus.PENDING, recurring, date, accountId);
    }

    public boolean isProcessed() {
        return status == ExpenseStatus.PROCESSED;
    }
}
