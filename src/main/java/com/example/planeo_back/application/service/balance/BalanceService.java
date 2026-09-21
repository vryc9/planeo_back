package com.example.planeo_back.application.service.balance;

import com.example.planeo_back.application.exception.account.AccountMessage;
import com.example.planeo_back.application.exception.account.InvalidAccountException;
import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.account.AccountDomain;
import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.ports.AccountRepository;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.domain.service.Guard;
import com.example.planeo_back.infrastructure.mapper.BalanceMapper;
import com.example.planeo_back.web.DTO.BalanceResponseDTO;
import com.example.planeo_back.web.DTO.balance.BalanceDTO;
import com.example.planeo_back.web.DTO.balance.DepositRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;

@Service
public class BalanceService {

    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private final AuthService authService;

    public BalanceService(ExpenseRepository expenseRepository,
                          AccountRepository accountRepository,
                          AuthService authService) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.authService = authService;
    }

    public BalanceResponseDTO getBalance() {
        return getBalance(authService.getUsername());
    }

    public BalanceResponseDTO getBalance(String username) {
        BigDecimal currentBalance = accountRepository.findByUsername(username).stream()
                .map(AccountDomain::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal pendingSum = expenseRepository.sumByUserIdAndStatus(username, ExpenseStatus.PENDING);
        BigDecimal futureBalance = currentBalance.subtract(pendingSum).setScale(2, RoundingMode.HALF_UP);

        return new BalanceResponseDTO(currentBalance, futureBalance, pendingSum);
    }

    @Transactional
    public BalanceResponseDTO deposit(DepositRequestDTO dto) {
        String username = authService.getUsername();
        AccountDomain account = accountRepository.findById(dto.accountId())
                .orElseThrow(() -> new NoSuchElementException("Compte introuvable : " + dto.accountId()));
        if (!account.username().equals(username)) {
            throw new InvalidAccountException(AccountMessage.ACCOUNT_NOT_OWNED);
        }
        accountRepository.update(account.withAmount(account.amount().add(dto.amount())));
        return getBalance(username);
    }
}
