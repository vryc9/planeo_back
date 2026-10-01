package com.example.planeo_back.application.usecase.accountdeletion;

import com.example.planeo_back.application.exception.account.RecentAuthenticationRequiredException;
import com.example.planeo_back.domain.enums.UserAccountStatus;
import com.example.planeo_back.domain.events.AccountDeletionRequested;
import com.example.planeo_back.domain.models.useraccount.UserAccountDomain;
import com.example.planeo_back.domain.ports.AccountDeletionPublisher;
import com.example.planeo_back.domain.ports.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestAccountDeletionUseCaseTest {

    private static final Instant NOW = Instant.parse("2026-10-01T12:00:00Z");

    private final UserAccountRepository userAccounts = mock(UserAccountRepository.class);
    private final AccountDeletionPublisher publisher = mock(AccountDeletionPublisher.class);
    private final RequestAccountDeletionUseCase useCase = new RequestAccountDeletionUseCase(
            userAccounts, publisher, Clock.fixed(NOW, ZoneOffset.UTC), 300);

    @Test
    void marksAccountPendingAndPublishesEvent() {
        when(userAccounts.findByUsername("alice")).thenReturn(Optional.empty());
        when(userAccounts.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute("alice", Optional.of(NOW.minus(Duration.ofMinutes(1))));

        ArgumentCaptor<UserAccountDomain> saved = ArgumentCaptor.forClass(UserAccountDomain.class);
        verify(userAccounts).save(saved.capture());
        assertEquals(UserAccountStatus.PENDING_DELETION, saved.getValue().status());

        ArgumentCaptor<AccountDeletionRequested> event = ArgumentCaptor.forClass(AccountDeletionRequested.class);
        verify(publisher).publish(event.capture());
        assertEquals("alice", event.getValue().username());
        assertEquals(saved.getValue().id(), event.getValue().requestId());
    }

    @Test
    void rejectsWhenNeverReauthenticated() {
        when(userAccounts.findByUsername("alice")).thenReturn(Optional.empty());

        assertThrows(RecentAuthenticationRequiredException.class, () -> useCase.execute("alice", Optional.empty()));
        verifyNoInteractions(publisher);
    }

    @Test
    void rejectsStaleReauthentication() {
        when(userAccounts.findByUsername("alice")).thenReturn(Optional.empty());

        assertThrows(RecentAuthenticationRequiredException.class,
                () -> useCase.execute("alice", Optional.of(NOW.minus(Duration.ofMinutes(10)))));
        verifyNoInteractions(publisher);
    }

    @Test
    void isIdempotentWhileDeletionIsInFlight() {
        when(userAccounts.findByUsername("alice")).thenReturn(Optional.of(
                UserAccountDomain.requestDeletion("alice", "hash", NOW)));

        useCase.execute("alice", Optional.empty());

        verify(userAccounts, never()).save(any());
        verifyNoInteractions(publisher);
    }
}
