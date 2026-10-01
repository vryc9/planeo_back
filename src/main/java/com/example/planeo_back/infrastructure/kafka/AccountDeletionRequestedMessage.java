package com.example.planeo_back.infrastructure.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Wire format of account.deletion.requested (shared contract with planeo_auth and planeo_admin). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountDeletionRequestedMessage(String requestId, String username, String occurredAt) {
}
