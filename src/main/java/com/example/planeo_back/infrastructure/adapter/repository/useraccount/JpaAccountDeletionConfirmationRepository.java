package com.example.planeo_back.infrastructure.adapter.repository.useraccount;

import com.example.planeo_back.domain.enums.DeletionParticipant;
import com.example.planeo_back.infrastructure.adapter.repository.entity.AccountDeletionConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAccountDeletionConfirmationRepository extends JpaRepository<AccountDeletionConfirmation, Long> {
    boolean existsByRequestIdAndParticipant(String requestId, DeletionParticipant participant);

    List<AccountDeletionConfirmation> findByRequestId(String requestId);
}
