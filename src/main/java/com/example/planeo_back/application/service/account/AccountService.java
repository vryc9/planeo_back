package com.example.planeo_back.application.service.account;

import com.example.planeo_back.application.exception.account.AccountMessage;
import com.example.planeo_back.application.exception.account.InvalidAccountException;
import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.domain.models.account.AccountDomain;
import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.ports.AccountRepository;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.infrastructure.mapper.AccountMapper;
import com.example.planeo_back.web.DTO.account.AccountCreateRequestDTO;
import com.example.planeo_back.web.DTO.account.AccountDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final BalanceRepository balanceRepository;
    private final AuthService authService;

    public AccountService(AccountRepository repository, AccountMapper mapper, BalanceRepository balanceRepository, AuthService authService) {
        this.repository = repository;
        this.mapper = mapper;
        this.balanceRepository = balanceRepository;
        this.authService = authService;
    }

    @Transactional
    public List<AccountDTO> save(List<AccountCreateRequestDTO> accounts) {
        String username = authService.getUsername();
        BalanceDomain balance = balanceRepository.findBalanceByUsername(username);

        BigDecimal sum = accounts.stream()
                .map(AccountCreateRequestDTO::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.compareTo(balance.currentBalance()) != 0) {
            throw new InvalidAccountException(AccountMessage.AMOUNT_SUM_MISMATCH);
        }

        List<AccountDomain> saved = accounts.stream()
                .map(dto -> repository.save(AccountDomain.build(username, dto.label(), dto.amount(), dto.logo())))
                .toList();

        return mapper.toDTO(saved);
    }

    public List<AccountDTO> getAccounts() {
        return mapper.toDTO(repository.findByUsername(authService.getUsername()));
    }

    public boolean accountsExistForUser() {
        return repository.accountsExistForUser(authService.getUsername());
    }
}
