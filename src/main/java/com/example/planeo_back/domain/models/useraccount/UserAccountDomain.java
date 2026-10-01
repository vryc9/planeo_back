package com.example.planeo_back.domain.models.useraccount;

import com.example.planeo_back.domain.enums.UserAccountStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Lifecycle of a user account as seen by planeo_back. A user without a row is ACTIVE.
 * Once DELETED the username is cleared (only its hash remains as proof of erasure),
 * so the same username can be registered again later.
 */
public record UserAccountDomain(
        String id,
        String username,
        String usernameHash,
        UserAccountStatus status,
        Instant deletionRequestedAt,
        Instant deletedAt
) {
    public static UserAccountDomain requestDeletion(String username, String usernameHash, Instant now) {
        return new UserAccountDomain(UUID.randomUUID().toString(), username, usernameHash,
                UserAccountStatus.PENDING_DELETION, now, null);
    }

    public boolean isPendingDeletion() {
        return status == UserAccountStatus.PENDING_DELETION;
    }
}
