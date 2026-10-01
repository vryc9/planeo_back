package com.example.planeo_back.application.usecase.accountdeletion;

import com.example.planeo_back.application.exception.account.RecentAuthenticationRequiredException;
import com.example.planeo_back.domain.events.AccountDeletionRequested;
import com.example.planeo_back.domain.models.useraccount.UserAccountDomain;
import com.example.planeo_back.domain.ports.AccountDeletionPublisher;
import com.example.planeo_back.domain.ports.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

/**
 * Starts the deletion of the caller's account: flips it to PENDING_DELETION and records
 * AccountDeletionRequested in the outbox, both in the same transaction.
 */
@Service
public class RequestAccountDeletionUseCase {

    private static final Logger log = LoggerFactory.getLogger(RequestAccountDeletionUseCase.class);

    private final UserAccountRepository userAccounts;
    private final AccountDeletionPublisher publisher;
    private final Clock clock;
    private final Duration reauthMaxAge;

    public RequestAccountDeletionUseCase(UserAccountRepository userAccounts,
                                         AccountDeletionPublisher publisher,
                                         Clock clock,
                                         @Value("${planeo.account-deletion.reauth-max-age-seconds}") long reauthMaxAgeSeconds) {
        this.userAccounts = userAccounts;
        this.publisher = publisher;
        this.clock = clock;
        this.reauthMaxAge = Duration.ofSeconds(reauthMaxAgeSeconds);
    }

    @Transactional
    public void execute(String username, Optional<Instant> lastReauthenticatedAt) {
        // Idempotent: a second call while the deletion is in flight changes nothing.
        if (userAccounts.findByUsername(username).filter(UserAccountDomain::isPendingDeletion).isPresent()) {
            return;
        }

        Instant now = clock.instant();
        boolean recentlyReauthenticated = lastReauthenticatedAt
                .filter(at -> !at.isAfter(now.plusSeconds(5)))
                .filter(at -> Duration.between(at, now).compareTo(reauthMaxAge) <= 0)
                .isPresent();
        if (!recentlyReauthenticated) {
            throw new RecentAuthenticationRequiredException();
        }

        UserAccountDomain account = userAccounts.save(
                UserAccountDomain.requestDeletion(username, sha256(username), now));
        publisher.publish(new AccountDeletionRequested(account.id(), username, now));
        log.info("Suppression de compte demandée (requestId={})", account.id());
    }

    private static String sha256(String value) {
        try {
            return HexFormat.of().formatHex(
                    MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
