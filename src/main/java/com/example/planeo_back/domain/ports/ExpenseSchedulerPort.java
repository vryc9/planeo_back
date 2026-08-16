package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.models.expense.ExpenseDomain;

public interface ExpenseSchedulerPort {
    void schedule(ExpenseDomain expense, String username);
    void cancel(Long id);
}
