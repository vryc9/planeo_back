package com.example.planeo_back.domain.enums;

import java.util.Set;

/**
 * Services that must each confirm they erased their share of a user's data
 * before the account can move to DELETED.
 */
public enum DeletionParticipant {
    AUTH,
    ADMIN,
    BACK;

    public static final Set<DeletionParticipant> ALL = Set.of(values());
}
