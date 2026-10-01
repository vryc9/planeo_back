package com.example.planeo_back.infrastructure.kafka;

import com.example.planeo_back.application.usecase.accountdeletion.ConfirmAccountDeletionUseCase;
import com.example.planeo_back.application.usecase.accountdeletion.PurgeUserFinancialDataUseCase;
import com.example.planeo_back.domain.enums.DeletionParticipant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Inbound Kafka adapters of the account deletion flow. Failures bubble up to retry / DLT. */
@Component
public class AccountDeletionListeners {

    private final ObjectMapper objectMapper;
    private final PurgeUserFinancialDataUseCase purge;
    private final ConfirmAccountDeletionUseCase confirm;

    public AccountDeletionListeners(ObjectMapper objectMapper,
                                    PurgeUserFinancialDataUseCase purge,
                                    ConfirmAccountDeletionUseCase confirm) {
        this.objectMapper = objectMapper;
        this.purge = purge;
        this.confirm = confirm;
    }

    @KafkaListener(topics = AccountDeletionTopics.REQUESTED)
    public void onDeletionRequested(String payload) {
        AccountDeletionRequestedMessage message = read(payload, AccountDeletionRequestedMessage.class);
        if (isBlank(message.requestId()) || isBlank(message.username())) {
            throw new InvalidMessageException("account.deletion.requested incomplet", null);
        }
        purge.execute(message.requestId(), message.username());
    }

    @KafkaListener(topics = AccountDeletionTopics.DATA_DELETED)
    public void onDataDeleted(String payload) {
        AccountDataDeletedMessage message = read(payload, AccountDataDeletedMessage.class);
        if (isBlank(message.requestId()) || isBlank(message.service())) {
            throw new InvalidMessageException("account.data.deleted incomplet", null);
        }
        DeletionParticipant participant;
        try {
            participant = DeletionParticipant.valueOf(message.service());
        } catch (IllegalArgumentException e) {
            throw new InvalidMessageException("Service inconnu : " + message.service(), e);
        }
        confirm.execute(message.requestId(), participant);
    }

    private <T> T read(String payload, Class<T> type) {
        try {
            return objectMapper.readValue(payload, type);
        } catch (JsonProcessingException e) {
            throw new InvalidMessageException("Payload illisible pour " + type.getSimpleName(), e);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
