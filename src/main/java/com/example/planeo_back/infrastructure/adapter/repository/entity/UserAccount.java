package com.example.planeo_back.infrastructure.adapter.repository.entity;

import com.example.planeo_back.domain.enums.UserAccountStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "user_account", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_account_username", columnNames = "username")
})
public class UserAccount {

    @Id
    @Column(length = 36)
    private String id;

    /** Cleared once the account is DELETED (nullable columns may repeat under a unique index). */
    @Column
    private String username;

    @Column(name = "username_hash", nullable = false, length = 64)
    private String usernameHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private UserAccountStatus status;

    @Column(name = "deletion_requested_at")
    private Instant deletionRequestedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public UserAccount() {
    }

    public UserAccount(String id, String username, String usernameHash, UserAccountStatus status,
                       Instant deletionRequestedAt, Instant deletedAt) {
        this.id = id;
        this.username = username;
        this.usernameHash = usernameHash;
        this.status = status;
        this.deletionRequestedAt = deletionRequestedAt;
        this.deletedAt = deletedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUsernameHash() { return usernameHash; }
    public void setUsernameHash(String usernameHash) { this.usernameHash = usernameHash; }
    public UserAccountStatus getStatus() { return status; }
    public void setStatus(UserAccountStatus status) { this.status = status; }
    public Instant getDeletionRequestedAt() { return deletionRequestedAt; }
    public void setDeletionRequestedAt(Instant deletionRequestedAt) { this.deletionRequestedAt = deletionRequestedAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }
}
