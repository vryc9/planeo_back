package com.example.planeo_back.infrastructure.adapter.repository.entity;

import com.example.planeo_back.domain.enums.DeletionParticipant;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "account_deletion_confirmation", uniqueConstraints = {
        @UniqueConstraint(name = "uk_deletion_confirmation", columnNames = {"request_id", "participant"})
})
public class AccountDeletionConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id", nullable = false, length = 36)
    private String requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DeletionParticipant participant;

    @Column(name = "confirmed_at", nullable = false)
    private Instant confirmedAt;

    public AccountDeletionConfirmation() {
    }

    public AccountDeletionConfirmation(String requestId, DeletionParticipant participant, Instant confirmedAt) {
        this.requestId = requestId;
        this.participant = participant;
        this.confirmedAt = confirmedAt;
    }

    public Long getId() { return id; }
    public String getRequestId() { return requestId; }
    public DeletionParticipant getParticipant() { return participant; }
    public Instant getConfirmedAt() { return confirmedAt; }
}
