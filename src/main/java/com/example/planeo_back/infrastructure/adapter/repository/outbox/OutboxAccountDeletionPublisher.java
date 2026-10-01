package com.example.planeo_back.infrastructure.adapter.repository.outbox;

import com.example.planeo_back.domain.events.AccountDeletionRequested;
import com.example.planeo_back.domain.ports.AccountDeletionPublisher;
import com.example.planeo_back.infrastructure.adapter.repository.entity.OutboxEvent;
import com.example.planeo_back.infrastructure.kafka.AccountDeletionRequestedMessage;
import com.example.planeo_back.infrastructure.kafka.AccountDeletionTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Writes the event to the outbox table in the caller's transaction (MANDATORY): the event
 * exists if and only if the business change was committed. The relay ships it to Kafka.
 */
@Component
public class OutboxAccountDeletionPublisher implements AccountDeletionPublisher {

    private final JpaOutboxEventRepository outbox;
    private final ObjectMapper objectMapper;

    public OutboxAccountDeletionPublisher(JpaOutboxEventRepository outbox, ObjectMapper objectMapper) {
        this.outbox = outbox;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void publish(AccountDeletionRequested event) {
        try {
            String payload = objectMapper.writeValueAsString(new AccountDeletionRequestedMessage(
                    event.requestId(), event.username(), event.occurredAt().toString()));
            outbox.save(new OutboxEvent(UUID.randomUUID().toString(), AccountDeletionTopics.REQUESTED,
                    event.username(), payload, event.occurredAt()));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Sérialisation de AccountDeletionRequested impossible", e);
        }
    }
}
