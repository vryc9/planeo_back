package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.enums.DeletionParticipant;

import java.time.Instant;
import java.util.Set;

public interface AccountDeletionConfirmationRepository {
    /** Records a confirmation; recording the same one twice is a no-op. */
    void record(String requestId, DeletionParticipant participant, Instant confirmedAt);

    Set<DeletionParticipant> findParticipants(String requestId);
}
