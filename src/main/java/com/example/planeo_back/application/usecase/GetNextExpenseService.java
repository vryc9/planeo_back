package com.example.planeo_back.application.usecase;
import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Balance;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.infrastructure.mapper.ExpenseMapper;
import com.example.planeo_back.infrastructure.sse.EventName;
import com.example.planeo_back.infrastructure.sse.SseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class GetNextExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BalanceRepository balanceRepository;
    private final SseService sseService;
    private final ExpenseMapper mapper;

    public GetNextExpenseService(ExpenseRepository expenseRepository, BalanceRepository balanceRepository, SseService sseService, ExpenseMapper mapper) {
        this.expenseRepository = expenseRepository;
        this.balanceRepository = balanceRepository;
        this.sseService = sseService;
        this.mapper = mapper;
    }

    public void processExpense(Long expenseId, String username) {
        ExpenseDomain expense = expenseRepository.findById(expenseId).orElseThrow();

        if (expense.isProcessed()) return;

        ExpenseDomain processed = expense.markAsProcessed();
        expenseRepository.save(processed);

        balanceRepository.decreaseCurrentBalance(username, expense.amount());
        updateFutureBalance(username);

        sseService.send(username, EventName.UPDATED_EXPENSE,
                "Modification de l'expense: " + expense.label());
    }

    private void updateFutureBalance(String username) {
        BalanceDomain balance = balanceRepository.findBalanceByUsername(username);
        BigDecimal pendingExpensesSum = expenseRepository.sumByUserIdAndStatus(username, ExpenseStatus.PENDING);
        balanceRepository.save(balance.withFutureBalance(pendingExpensesSum));
    }
}
