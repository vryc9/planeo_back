package com.example.planeo_back.infrastructure.adapter.repository.account;

import com.example.planeo_back.domain.models.account.AccountDomain;
import com.example.planeo_back.domain.ports.AccountRepository;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Account;
import com.example.planeo_back.infrastructure.mapper.AccountMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepositoryAdapter implements AccountRepository {
    private final JpaAccountRepository repository;
    private final AccountMapper mapper;

    public AccountRepositoryAdapter(JpaAccountRepository repository, AccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<AccountDomain> findById(Long id) {
        return repository.findById(id).map(mapper::fromEntityToDomain);
    }

    @Override
    public List<AccountDomain> findAll() {
        return repository.findAll().stream().map(mapper::fromEntityToDomain).toList();
    }

    @Override
    public AccountDomain save(AccountDomain account) {
        Account saved = repository.save(mapper.toEntity(account));
        return mapper.fromEntityToDomain(saved);
    }

    @Override
    public void delete(AccountDomain account) {
        repository.delete(mapper.toEntity(account));
    }

    @Override
    public List<AccountDomain> findByUsername(String username) {
        return repository.findByUsername(username).stream().map(mapper::fromEntityToDomain).toList();
    }

    @Override
    public boolean accountsExistForUser(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public AccountDomain update(AccountDomain domain) {
        return mapper.fromEntityToDomain(repository.save(mapper.toEntity(domain)));
    }
}
