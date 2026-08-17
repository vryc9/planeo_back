package com.example.planeo_back.application.service.expense;
import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.domain.models.category.CategoryDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.models.expense.ExpensesByCategoryDomain;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.domain.ports.ExpenseSchedulerPort;
import com.example.planeo_back.infrastructure.mapper.ExpenseMapper;
import com.example.planeo_back.infrastructure.scheduler.SchedulerService;
import com.example.planeo_back.domain.service.CalculateFutureBalance;
import com.example.planeo_back.web.DTO.ExpenseDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseAmountByCategoryDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseCreateRequestDTO;
import com.example.planeo_back.web.DTO.expense.ExpensePerMonthDTO;
import com.example.planeo_back.web.DTO.expense.ExpensesByCategoryDTO;
import jakarta.transaction.Transactional;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExpenseService{

    private final ExpenseRepository repository;
    private final ExpenseMapper mapper;
    private final BalanceRepository balanceRepository;
    private final AuthService authService;
    private final ExpenseSchedulerPort scheduler;

    public ExpenseService(ExpenseRepository repository, ExpenseMapper mapper, BalanceRepository balanceRepository, AuthService authService, ExpenseSchedulerPort scheduler) {
        this.repository = repository;
        this.mapper = mapper;
        this.balanceRepository = balanceRepository;
        this.authService = authService;
        this.scheduler = scheduler;
    }

    public ExpenseDTO findById(Long id) {
        return repository.findById(id).map(mapper::fromDomainToDTO).orElseThrow(NoSuchElementException::new);
    }

    public List<ExpenseDTO> getExpensesForCurrentMonth() {
        return repository.findExpenseForCurrentMonth(authService.getUsername())
                .stream()
                .map(mapper::fromDomainToDTO)
                .toList();
    }

    public List<ExpenseDTO> getExpensesForLastMonths(int monthsCount) {
        if (monthsCount < 1) {
            throw new IllegalArgumentException("monthsCount must be >= 1, got: " + monthsCount);
        }
        return repository.findExpenseForLastMonths(authService.getUsername(), monthsCount)
                .stream()
                .map(mapper::fromDomainToDTO)
                .toList();
    }


    @Transactional
    public ExpenseDTO save(ExpenseCreateRequestDTO dto) throws SchedulerException {
        ExpenseDomain expenseDomain = new ExpenseDomain(null, authService.getUsername(), dto.amount(), dto.label(), CategoryDomain.buildWithId(dto.category().id(), dto.category().name(), dto.category().icon(), authService.getUsername()),
                isBeforeOfToday(dto.date()) ? ExpenseStatus.PROCESSED : ExpenseStatus.PENDING
                , dto.recurring(), dto.date());
        BalanceDomain balance = balanceRepository.findBalanceByUsername(authService.getUsername());
        BalanceDomain balanceUpdated = !isBeforeOfToday(expenseDomain.date())
                ? new BalanceDomain(
                balance.id(),
                balance.username(),
                balance.currentBalance(),
                CalculateFutureBalance.calculFutureBalance(
                        repository.findExpenseByUsernameAndStatus(authService.getUsername(), ExpenseStatus.PENDING),
                        balance,
                        dto.amount()),
                balance.pendingExpense())
                : new BalanceDomain(
                balance.id(),
                balance.username(),
                balance.currentBalance(),
                balance.futureBalance(),
                balance.pendingExpense())
                .withCurrentBalanceUpadated(expenseDomain.amount());

        balanceRepository.save(balanceUpdated);
        ExpenseDomain savedExpense = repository.save(expenseDomain);
        scheduler.schedule(savedExpense, authService.getUsername());
        return mapper.fromDomainToDTO(savedExpense);
    }

    public void delete(ExpenseDTO expenseDTO) {
        String username = authService.getUsername();
        ExpenseDomain expense = mapper.fromDtoToDomain(expenseDTO);
        BalanceDomain balance = balanceRepository.findBalanceByUsername(username);

        BigDecimal add = balance.futureBalance().add(expense.amount());
        BalanceDomain updated =  expense.isProcessed() ?
                new BalanceDomain(
                        balance.id(),
                        balance.username(),
                        balance.currentBalance().add(expense.amount()),
                        add,
                        balance.pendingExpense()
                ) :
                new BalanceDomain(
                balance.id(),
                balance.username(),
                balance.currentBalance(),
                        add,
                balance.pendingExpense()
        );
        balanceRepository.save(updated);
        repository.delete(expense);
    }


    @Transactional
    public ExpenseDTO update(ExpenseDTO dto) {
        if (dto.id() == null) {
            throw new NoSuchElementException();
        }
        ExpenseDomain existing = repository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("Dépense introuvable : " + dto.id()));

        CategoryDomain category = CategoryDomain.buildWithId(dto.category().id(), authService.getUsername(),
                dto.category().icon(), dto.category().name());

        boolean wasProcessed = existing.isProcessed();
        boolean movingToFuture = !isBeforeOfToday(dto.date());

        if (wasProcessed && !movingToFuture) {
            ExpenseDomain domain = existing.withUpdatedDetails(dto.amount(), dto.label(), category, dto.recurring(), dto.date());
            ExpenseDomain saved = repository.update(domain);
            return mapper.fromDomainToDTO(saved);
        }

        ExpenseDomain domain;
        BigDecimal newCurrentBalance;
        BalanceDomain balance = balanceRepository.findBalanceByUsername(authService.getUsername());
        if (wasProcessed) {
            domain = existing.withUpdatedDetails(dto.amount(), dto.label(), category, dto.recurring(), dto.date())
                    .reopen();
            newCurrentBalance = balance.currentBalance().add(existing.amount());
        } else if (movingToFuture) {
            domain = existing.withUpdatedDetails(dto.amount(), dto.label(), category, dto.recurring(), dto.date());
            newCurrentBalance = balance.currentBalance();
        } else {
            domain = existing.withUpdatedDetails(dto.amount(), dto.label(), category, dto.recurring(), dto.date())
                    .markAsProcessed();
            newCurrentBalance = balance.currentBalance().subtract(dto.amount());
        }

        ExpenseDomain saved = repository.update(domain);
        BigDecimal pendingSum = repository
                .findExpenseByUsernameAndStatus(authService.getUsername(), ExpenseStatus.PENDING)
                .stream()
                .map(ExpenseDomain::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BalanceDomain balanceUpdated = new BalanceDomain(
                balance.id(), balance.username(), newCurrentBalance, balance.futureBalance(), balance.pendingExpense())
                .withFutureBalance(pendingSum);
        balanceRepository.save(balanceUpdated);

        if (saved.isProcessed()) {
            scheduler.cancel(saved.id());
        } else {
            scheduler.schedule(saved, authService.getUsername());
        }
        return mapper.fromDomainToDTO(saved);
    }


    public List<ExpensePerMonthDTO> getExpensePerMonths() {
        return mapper.transformExpensePerMonthDTO(repository.getExpensePerMonthByUser(authService.getUsername()));
    }

    public List<ExpenseAmountByCategoryDTO> getExpenseAmountByCategory() {
        return mapper.transformExpenseAmountByCategories(repository.getExpenseAmountByCategory(authService.getUsername()));
    }

    public List<ExpensesByCategoryDTO> getExpensesByCategory() {
        return mapper.transformExpensesCategoryToDTO(repository.getExpensesByCategory(authService.getUsername()));
    }

    private boolean isBeforeOfToday (LocalDate date) {
        return !date.isAfter(LocalDate.now());
    }
}
