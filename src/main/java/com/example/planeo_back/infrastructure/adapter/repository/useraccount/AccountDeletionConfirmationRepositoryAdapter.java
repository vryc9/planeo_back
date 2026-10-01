package com.example.planeo_back.infrastructure.adapter.repository.useraccount;

import com.example.planeo_back.domain.enums.DeletionParticipant;
import com.example.planeo_back.domain.ports.AccountDeletionConfirmationRepository;
import com.example.planeo_back.infrastructure.adapter.repository.entity.AccountDeletionConfirmation;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

@Repository
public class AccountDeletionConfirmationRepositoryAdapter implements AccountDeletionConfirmationRepository {

    private final JpaAccountDeletionConfirmationRepository repository;

    public AccountDeletionConfirmationRepositoryAdapter(JpaAccountDeletionConfirmationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void record(String requestId, DeletionParticipant participant, Instant confirmedAt) {
        if (!repository.existsByRequestIdAndParticipant(requestId, participant)) {
            repository.save(new AccountDeletionConfirmation(requestId, participant, confirmedAt));
        }
    }

    @Override
    public Set<DeletionParticipant> findParticipants(String requestId) {
        Set<DeletionParticipant> result = EnumSet.noneOf(DeletionParticipant.class);
        repository.findByRequestId(requestId).forEach(c -> result.add(c.getParticipant()));
        return result;
    }
}
