package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.models.useraccount.UserAccountDomain;

import java.time.Instant;
import java.util.Optional;

public interface UserAccountRepository {
    Optional<UserAccountDomain> findByUsername(String username);

    Optional<UserAccountDomain> findById(String id);

    /** Same as {@link #findById} but takes a write lock to serialise concurrent confirmations. */
    Optional<UserAccountDomain> findByIdForUpdate(String id);

    boolean isBlocked(String username);

    UserAccountDomain save(UserAccountDomain account);

    void markDeleted(String id, Instant deletedAt);
}
