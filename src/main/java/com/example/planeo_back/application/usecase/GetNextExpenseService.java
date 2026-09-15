package com.example.planeo_back.application.usecase;
import com.example.planeo_back.domain.models.account.AccountDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.ports.AccountRepository;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
public class GetNextExpenseService {
    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private static final Logger log = LoggerFactory.getLogger(GetNextExpenseService.class);


    public GetNextExpenseService(ExpenseRepository expenseRepository, AccountRepository accountRepository) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
    }

    public void processExpense(Long expenseId, String username) {
        log.info("Entrée dans la méthode processExpense");
        ExpenseDomain expense = expenseRepository.findById(expenseId).orElseThrow();

        if (expense.isProcessed()) return;

        ExpenseDomain processed = expense.markAsProcessed();
        expenseRepository.save(processed);

        if (expense.accountId() != null) {
            AccountDomain account = accountRepository.findById(expense.accountId())
                    .orElseThrow(() -> new NoSuchElementException("Compte introuvable : " + expense.accountId()));
            accountRepository.update(account.withAmount(account.amount().subtract(expense.amount())));
        }
    }
}
