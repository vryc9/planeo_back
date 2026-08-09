package com.example.planeo_back.infrastructure.adapter.repository.expense;

import com.example.planeo_back.domain.models.ExpenseAmountByTagDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.models.expense.ExpensesByTagDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.ExpensePerMount;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.infrastructure.mapper.ExpenseMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ExpenseRepositoryAdapter implements ExpenseRepository {
    private final JpaExpenseRepository repository;
    private final ExpenseMapper mapper;

    public ExpenseRepositoryAdapter(JpaExpenseRepository repository, ExpenseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ExpenseDomain> findById(Long id) {
        return repository.findById(id).map(mapper::fromEntityToDomain);
    }

    @Override
    public List<ExpenseDomain> findAll() {
        return repository.findAll().stream().map(mapper::fromEntityToDomain).toList();
    }

    @Override
    public ExpenseDomain save(ExpenseDomain expense) {
        Expense entity = mapper.toEntity(expense);
        Expense saved = repository.save(entity);
        return mapper.fromEntityToDomain(saved);
    }

    public ExpenseDomain update(ExpenseDomain expense) {
        return mapper.fromEntityToDomain(repository.save(mapper.toEntity(expense)));
    }

    @Override
    public void delete(ExpenseDomain expense) {
        repository.delete(mapper.toEntity(expense));
    }

    @Override
    public List<ExpenseDomain> findExpenseByUsername(String username) {
        return repository.findExpenseByUsernameOrderByDateDesc(username).stream().map(mapper::fromEntityToDomain).toList();
    }

    @Override
    public BigDecimal sumByUserIdAndStatus(String username, ExpenseStatus status) {
        return repository.sumByUserIdAndStatus(username, status);
    }

    @Override
    public List<ExpensePerMount> getExpensePerMonthByUser(String username) {
        return repository.getExpensePerMountByUser(username);
    }

    @Override
    public List<ExpenseAmountByTagDomain> getExpenseAmountByTag(String username) {
        return repository.findTotalAmountByTagForCurrentMonth(username);
    }

    @Override
    public List<ExpensesByTagDomain> getExpensesByTags(String username) {
        return findExpenseForCurrentMonth(username).stream()
                .collect(Collectors.groupingBy(ExpenseDomain::tag, Collectors.toUnmodifiableList()))
                .entrySet().stream()
                .map(entry -> new ExpensesByTagDomain(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(ExpensesByTagDomain::tag))
                .toList();
    }

    @Override
    public List<ExpenseDomain> findExpenseForCurrentMonth(String username) {
        return findExpenseForLastMonths(username, 1);
    }

    @Override
    public List<ExpenseDomain> findExpenseForLastMonths(String username, int monthsCount) {
        LocalDate startDate = YearMonth.now()
                .minusMonths(monthsCount - 1)
                .atDay(1);
        return repository.findExpenseByUsernameSince(username, startDate).stream()
                .map(mapper::fromEntityToDomain)
                .toList();
    }

    @Override
    public List<ExpenseDomain> findExpenseByUsernameAndStatus(String username, ExpenseStatus status) {
        return repository.findExpenseByUsernameAndStatus(username, status).stream().map(mapper::fromEntityToDomain).toList();
    }
}
