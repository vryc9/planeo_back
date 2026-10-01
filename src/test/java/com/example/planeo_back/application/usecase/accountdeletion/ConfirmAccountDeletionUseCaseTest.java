package com.example.planeo_back.application.usecase.accountdeletion;

import com.example.planeo_back.domain.enums.DeletionParticipant;
import com.example.planeo_back.domain.models.useraccount.UserAccountDomain;
import com.example.planeo_back.domain.ports.AccountDeletionConfirmationRepository;
import com.example.planeo_back.domain.ports.UserAccountRepository;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.EnumSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ConfirmAccountDeletionUseCaseTest {

    private static final Instant NOW = Instant.parse("2026-10-01T12:00:00Z");

    private final UserAccountRepository userAccounts = mock(UserAccountRepository.class);
    private final AccountDeletionConfirmationRepository confirmations = mock(AccountDeletionConfirmationRepository.class);
    private final ConfirmAccountDeletionUseCase useCase = new ConfirmAccountDeletionUseCase(
            userAccounts, confirmations, Clock.fixed(NOW, ZoneOffset.UTC));

    private UserAccountDomain pending() {
        return UserAccountDomain.requestDeletion("alice", "hash", NOW);
    }

    @Test
    void staysPendingUntilEveryParticipantConfirmed() {
        UserAccountDomain account = pending();
        when(userAccounts.findByIdForUpdate(account.id())).thenReturn(Optional.of(account));
        when(confirmations.findParticipants(account.id()))
                .thenReturn(EnumSet.of(DeletionParticipant.AUTH, DeletionParticipant.BACK));

        useCase.execute(account.id(), DeletionParticipant.AUTH);

        verify(confirmations).record(account.id(), DeletionParticipant.AUTH, NOW);
        verify(userAccounts, never()).markDeleted(any(), any());
    }

    @Test
    void marksDeletedOnLastConfirmation() {
        UserAccountDomain account = pending();
        when(userAccounts.findByIdForUpdate(account.id())).thenReturn(Optional.of(account));
        when(confirmations.findParticipants(account.id())).thenReturn(EnumSet.allOf(DeletionParticipant.class));

        useCase.execute(account.id(), DeletionParticipant.ADMIN);

        verify(userAccounts).markDeleted(eq(account.id()), eq(NOW));
    }

    @Test
    void ignoresUnknownRequest() {
        when(userAccounts.findByIdForUpdate("nope")).thenReturn(Optional.empty());

        useCase.execute("nope", DeletionParticipant.AUTH);

        verifyNoInteractions(confirmations);
    }
}
