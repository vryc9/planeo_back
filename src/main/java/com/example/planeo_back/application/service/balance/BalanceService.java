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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BalanceService {

    private final BalanceRepository repository;
    private final BalanceMapper mapper;
    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;
    private final AuthService authService;

    public BalanceService(BalanceRepository balanceRepository, BalanceMapper balanceMapper, ExpenseRepository expenseRepository, AccountRepository accountRepository, AuthService authService) {
        this.repository = balanceRepository;
        this.mapper = balanceMapper;
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
        this.authService  = authService;
    }

    public BalanceResponseDTO findById(Long id) {
        BalanceDomain balance = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return toResponse(balance);
    }

    public List<BalanceResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BalanceResponseDTO save(BalanceDTO balanceDTO) throws IllegalAccessException {
        Guard.checkIfObjectIsNull(balanceDTO);
        String username = authService.getUsername();
        BalanceDomain balance = new BalanceDomain(null, username, balanceDTO.currentBalance(), balanceDTO.futureBalance(), BigDecimal.ZERO);
        return mapper.toDTO(repository.save(balance));
    }

    public void delete(BalanceDTO balanceDTO) {
        BalanceDomain balance = mapper.fromDtoToDomain(balanceDTO);
        repository.delete(balance);
    }

    public BalanceResponseDTO getBalance(String username) {
        BalanceDomain balance = repository.findBalanceByUsername(username);
        return toResponse(balance);
    }

    public boolean balanceExistForUser() {
        return repository.balanceExistForUser(authService.getUsername());
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

    // Tant qu'aucun compte n'existe (onboarding en cours), on retombe sur le solde saisi via /balance.
    private BalanceResponseDTO toResponse(BalanceDomain balance) {
        BigDecimal pendingSum = expenseRepository.sumByUserIdAndStatus(balance.username(), ExpenseStatus.PENDING);
        BigDecimal currentBalance = accountRepository.accountsExistForUser(balance.username())
                ? sumAccounts(balance.username())
                : balance.currentBalance();

        BalanceDomain derived = new BalanceDomain(balance.id(), balance.username(), currentBalance, balance.futureBalance(), balance.pendingExpense())
                .withFutureBalance(pendingSum);
        return new BalanceResponseDTO(derived.id(), derived.currentBalance(), derived.futureBalance(), pendingSum);
    }

    private BigDecimal sumAccounts(String username) {
        return accountRepository.findByUsername(username).stream()
                .map(AccountDomain::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
