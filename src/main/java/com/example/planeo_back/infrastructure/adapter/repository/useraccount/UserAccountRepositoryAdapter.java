package com.example.planeo_back.infrastructure.adapter.repository.useraccount;

import com.example.planeo_back.domain.enums.UserAccountStatus;
import com.example.planeo_back.domain.models.useraccount.UserAccountDomain;
import com.example.planeo_back.domain.ports.UserAccountRepository;
import com.example.planeo_back.infrastructure.adapter.repository.entity.UserAccount;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public class UserAccountRepositoryAdapter implements UserAccountRepository {

    private final JpaUserAccountRepository repository;

    public UserAccountRepositoryAdapter(JpaUserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserAccountDomain> findByUsername(String username) {
        return repository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public Optional<UserAccountDomain> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<UserAccountDomain> findByIdForUpdate(String id) {
        return repository.findByIdForUpdate(id).map(this::toDomain);
    }

    @Override
    public boolean isBlocked(String username) {
        return repository.existsByUsernameAndStatusNot(username, UserAccountStatus.ACTIVE);
    }

    @Override
    public UserAccountDomain save(UserAccountDomain a) {
        return toDomain(repository.save(new UserAccount(a.id(), a.username(), a.usernameHash(), a.status(),
                a.deletionRequestedAt(), a.deletedAt())));
    }

    @Override
    public void markDeleted(String id, Instant deletedAt) {
        repository.markDeleted(id, deletedAt);
    }

    private UserAccountDomain toDomain(UserAccount e) {
        return new UserAccountDomain(e.getId(), e.getUsername(), e.getUsernameHash(), e.getStatus(),
                e.getDeletionRequestedAt(), e.getDeletedAt());
    }
}
