package com.example.planeo_back.domain.events;

import java.time.Instant;

/**
 * Fact announced when a user asked for their account to be deleted.
 * {@code requestId} identifies the deletion request across all services.
 */
public record AccountDeletionRequested(String requestId, String username, Instant occurredAt) {
}
