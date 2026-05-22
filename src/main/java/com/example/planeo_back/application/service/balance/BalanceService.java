package com.example.planeo_back.application.service.balance;

import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.domain.service.Guard;
import com.example.planeo_back.infrastructure.mapper.BalanceMapper;
import com.example.planeo_back.web.DTO.BalanceResponseDTO;
import com.example.planeo_back.web.DTO.balance.BalanceDTO;
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
    private final AuthService authService;

    public BalanceService(BalanceRepository balanceRepository, BalanceMapper balanceMapper, ExpenseRepository expenseRepository, AuthService authService) {
        this.repository = balanceRepository;
        this.mapper = balanceMapper;
        this.expenseRepository = expenseRepository;
        this.authService  = authService;
    }

    public BalanceResponseDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public List<BalanceResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
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
        BigDecimal pendingSum = expenseRepository.sumByUserIdAndStatus(username, ExpenseStatus.PENDING);
        return new BalanceResponseDTO(
                balance.id(),
                balance.currentBalance(),
                balance.futureBalance(),
                pendingSum
        );
    }

    public boolean balanceExistForUser() {
        return repository.balanceExistForUser(authService.getUsername());
    }

    public BalanceResponseDTO update(BigDecimal newAmount) {
        String username = authService.getUsername();
        BigDecimal pendingSum = expenseRepository.sumByUserIdAndStatus(username, ExpenseStatus.PENDING);
        BalanceDomain balance = repository.findBalanceByUsername(username);
        BalanceDomain updated = repository.update(balance.withDeposit(newAmount, pendingSum));
        return BalanceResponseDTO.from(updated);
    }
}
