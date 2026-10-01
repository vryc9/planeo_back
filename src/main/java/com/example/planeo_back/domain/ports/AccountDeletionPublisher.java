package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.events.AccountDeletionRequested;

/**
 * Announces that an account deletion was requested. Implementations must take part in the
 * caller's transaction so the event is persisted atomically with the state change.
 */
public interface AccountDeletionPublisher {
    void publish(AccountDeletionRequested event);
}
