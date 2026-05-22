package com.example.planeo_back.application.service.expense;
import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.infrastructure.mapper.ExpenseMapper;
import com.example.planeo_back.infrastructure.scheduler.SchedulerService;
import com.example.planeo_back.domain.service.CalculateFutureBalance;
import com.example.planeo_back.infrastructure.mapper.BalanceMapper;
import com.example.planeo_back.web.DTO.ExpenseDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseByTagDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseCreateRequestDTO;
import com.example.planeo_back.web.DTO.expense.ExpensePerMonthDTO;
import jakarta.transaction.Transactional;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExpenseService{

    private final ExpenseRepository repository;
    private final ExpenseMapper mapper;
    private final BalanceRepository balanceRepository;
    private final AuthService authService;
    private final SchedulerService scheduler;

    public ExpenseService(ExpenseRepository repository, ExpenseMapper mapper, BalanceRepository balanceRepository, BalanceMapper balanceMapper, AuthService authService, SchedulerService scheduler) {
        this.repository = repository;
        this.mapper = mapper;
        this.balanceRepository = balanceRepository;
        this.authService = authService;
        this.scheduler = scheduler;
    }

    public ExpenseDTO findById(Long id) {
        return repository.findById(id).map(mapper::fromDomainToDTO).orElseThrow(NoSuchElementException::new);
    }

    public List<ExpenseDTO> findAll() {
        return repository.findExpenseByUsername(authService.getUsername())
                .stream()
                .map(mapper::fromDomainToDTO)
                .toList();
    }

    @Transactional
    public ExpenseDTO save(ExpenseCreateRequestDTO dto) throws SchedulerException {
        ExpenseDomain expenseDomain = new ExpenseDomain(null, authService.getUsername(), dto.amount(), dto.label(), dto.tag(),dto.status(), dto.recurring(), dto.date());

        BalanceDomain balance = balanceRepository.findBalanceByUsername(authService.getUsername());
        BalanceDomain balanceWithFutureBalance = new BalanceDomain(
                balance.id(),
                balance.username(),
                balance.currentBalance(),
                CalculateFutureBalance.calculFutureBalance(repository.findExpenseByUsername(authService.getUsername()), balance, dto.amount()),
                balance.pendingExpense());

        balanceRepository.save(balanceWithFutureBalance);
        ExpenseDomain savedExpense = repository.save(expenseDomain);
        //scheduler.scheduleJob(savedExpense, authService.getUsername());
        return mapper.fromDomainToDTO(savedExpense);
    }

    public void delete(ExpenseDTO expenseDTO) {
        String username = authService.getUsername();
        ExpenseDomain expense = mapper.fromDtoToDomain(expenseDTO);

        BalanceDomain balance = balanceRepository.findBalanceByUsername(username);
        BalanceDomain updated = new BalanceDomain(
                balance.id(),
                balance.username(),
                balance.currentBalance(),
                balance.futureBalance().add(expense.amount()),
                balance.pendingExpense()
        );
        balanceRepository.save(updated);
        repository.delete(expense);
    }

    public ExpenseDTO update(ExpenseDTO dto) {
        if(dto.id() == null) {
            throw new NoSuchElementException();
        }
        return mapper.fromDomainToDTO(repository.update(mapper.fromDtoToDomain(dto)));
    }

    public List<ExpensePerMonthDTO> getExpensePerMonths() {
        return mapper.transformExpensePerMonthDTO(repository.getExpensePerMonthByUser(authService.getUsername()));
    }

    public List<ExpenseByTagDTO> getExpenseByTags() {
        return mapper.transformExpenseByTags(repository.getExpenseByTag(authService.getUsername()));
    }
}
