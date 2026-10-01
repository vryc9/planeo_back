package com.example.planeo_back.infrastructure.kafka;

public final class AccountDeletionTopics {
    public static final String REQUESTED = "account.deletion.requested";
    public static final String DATA_DELETED = "account.data.deleted";

    private AccountDeletionTopics() {
    }
}
