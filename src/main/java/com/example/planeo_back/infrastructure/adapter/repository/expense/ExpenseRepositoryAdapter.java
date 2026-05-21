package com.example.planeo_back.infrastructure.adapter.repository.expense;

import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.ExpenseByTag;
import com.example.planeo_back.domain.models.ExpensePerMount;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.infrastructure.mapper.ExpenseMapper;
import com.example.planeo_back.web.DTO.expense.ExpenseCreateRequestDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
        return repository.findExpenseByUsername(username).stream().map(mapper::fromEntityToDomain).toList();
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
    public List<ExpenseByTag> getExpenseByTag(String username) {
        return repository.findTotalAmountByTagForCurrentMonth(username);
    }
}
