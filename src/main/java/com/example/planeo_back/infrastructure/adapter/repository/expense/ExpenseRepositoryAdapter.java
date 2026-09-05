package com.example.planeo_back.infrastructure.adapter.repository.expense;

import com.example.planeo_back.domain.models.ExpenseAmountByCategoryDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.models.expense.ExpensesByCategoryDomain;
import com.example.planeo_back.domain.models.expense.MonthlyExpensesByCategoryDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.ExpensePerMonthDomain;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.infrastructure.mapper.CategoryMapper;
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
    private final CategoryMapper categoryMapper;

    public ExpenseRepositoryAdapter(JpaExpenseRepository repository, ExpenseMapper mapper, CategoryMapper categoryMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoryMapper = categoryMapper;
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
        entity.setCategory(categoryMapper.toEntity(expense.category()));
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
    public List<ExpensePerMonthDomain> getExpensePerMonthByUser(String username) {
        return repository.getExpensePerMonthByUser(username).stream().map(e -> ExpensePerMonthDomain.build(e.month(), e.total())).toList();
    }

    @Override
    public List<ExpenseAmountByCategoryDomain> getExpenseAmountByCategory(String username) {
        return repository.findTotalAmountByCategoryForCurrentMonth(username).
                stream().
                map(c -> ExpenseAmountByCategoryDomain.build(c.categoryId(),c.categoryName(), c.categoryIcon(), username, c.totalAmount())).toList();
    }

    @Override
    public List<ExpensesByCategoryDomain> getExpensesByCategory(String username) {
        return groupByCategory(findExpenseForCurrentMonth(username));
    }

    @Override
    public List<MonthlyExpensesByCategoryDomain> getExpensesByCategoryForLastTwoMonths(String username) {
        return findExpenseForLastMonths(username, 2).stream()
                .collect(Collectors.groupingBy(e -> YearMonth.from(e.date())))
                .entrySet().stream()
                .map(entry -> new MonthlyExpensesByCategoryDomain(entry.getKey(), groupByCategory(entry.getValue())))
                .sorted(Comparator.comparing(MonthlyExpensesByCategoryDomain::month).reversed())
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

    private List<ExpensesByCategoryDomain> groupByCategory(List<ExpenseDomain> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(ExpenseDomain::category, Collectors.toUnmodifiableList()))
                .entrySet().stream()
                .map(entry -> new ExpensesByCategoryDomain(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(dto -> dto.category().name()))
                .toList();
    }
}
