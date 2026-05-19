package com.example.planeo_back.infrastructure.adapter.repository.balance;

import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Balance;
import com.example.planeo_back.domain.ports.BalanceRepository;
import com.example.planeo_back.infrastructure.mapper.BalanceMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class BalanceRepositoryAdapter implements BalanceRepository {
    private final JpaBalanceRepository repository;
    private final BalanceMapper mapper;

    public BalanceRepositoryAdapter(JpaBalanceRepository jpaBalanceRepository, BalanceMapper mapper) {
        this.repository = jpaBalanceRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<BalanceDomain> findById(Long id) {
        return repository.findById(id).map(mapper::fromEntityToDomain);
    }

    @Override
    public List<BalanceDomain> findAll() {
        return repository.findAll().stream().map(mapper::fromEntityToDomain).toList();
    }

    @Override
    public BalanceDomain save(BalanceDomain balance) {
        Balance entity = mapper.toEntity(balance);
        Balance saved = repository.save(entity);
        return mapper.fromEntityToDomain(saved);
    }

    @Override
    public void delete(BalanceDomain balance) {
        repository.delete(mapper.toEntity(balance));
    }

    @Override
    public BalanceDomain findBalanceByUsername(String username) {
        return mapper.fromEntityToDomain(repository.findBalanceByUsername(username));
    }

    @Override
    public boolean balanceExistForUser(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public void decreaseCurrentBalance(String username, BigDecimal amount) {
        repository.decreaseCurrentBalance(username, amount);
    }
}
