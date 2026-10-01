package com.example.planeo_back.infrastructure.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Wire format of account.data.deleted: one service confirms it erased its share of the data. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AccountDataDeletedMessage(String requestId, String service, String deletedAt) {
}
