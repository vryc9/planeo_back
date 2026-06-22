package com.example.planeo_back.application.usecase;
import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class GetNextExpenseService {
    private final ExpenseRepository expenseRepository;
    private final BalanceRepository balanceRepository;
    private static final Logger log = LoggerFactory.getLogger(GetNextExpenseService.class);


    public GetNextExpenseService(ExpenseRepository expenseRepository, BalanceRepository balanceRepository) {
        this.expenseRepository = expenseRepository;
        this.balanceRepository = balanceRepository;
    }

    public void processExpense(Long expenseId, String username) {
        log.info("Entrée dans la méthode processExpense");
        ExpenseDomain expense = expenseRepository.findById(expenseId).orElseThrow();

        if (expense.isProcessed()) return;

        ExpenseDomain processed = expense.markAsProcessed();
        expenseRepository.save(processed);

        balanceRepository.decreaseCurrentBalance(username, expense.amount());
        updateFutureBalance(username);
    }

    private void updateFutureBalance(String username) {
        BalanceDomain balance = balanceRepository.findBalanceByUsername(username);
        BigDecimal pendingExpensesSum = expenseRepository.sumByUserIdAndStatus(username, ExpenseStatus.PENDING);
        balanceRepository.save(balance.withFutureBalance(pendingExpensesSum));
    }
}
