package com.example.planeo_back.application.usecase.accountdeletion;

import com.example.planeo_back.domain.enums.DeletionParticipant;
import com.example.planeo_back.domain.models.useraccount.UserAccountDomain;
import com.example.planeo_back.domain.ports.AccountDeletionConfirmationRepository;
import com.example.planeo_back.domain.ports.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Optional;

/**
 * Registers that a service erased its data. Once every participant confirmed, the account
 * moves to DELETED (the audit trail kept for GDPR purposes).
 */
@Service
public class ConfirmAccountDeletionUseCase {

    private static final Logger log = LoggerFactory.getLogger(ConfirmAccountDeletionUseCase.class);

    private final UserAccountRepository userAccounts;
    private final AccountDeletionConfirmationRepository confirmations;
    private final Clock clock;

    public ConfirmAccountDeletionUseCase(UserAccountRepository userAccounts,
                                         AccountDeletionConfirmationRepository confirmations,
                                         Clock clock) {
        this.userAccounts = userAccounts;
        this.confirmations = confirmations;
        this.clock = clock;
    }

    @Transactional
    public void execute(String requestId, DeletionParticipant participant) {
        // The lock must be the first statement of the transaction: it serialises concurrent
        // confirmations so that the last one always sees all the others.
        Optional<UserAccountDomain> account = userAccounts.findByIdForUpdate(requestId);
        if (account.isEmpty()) {
            log.warn("Confirmation {} ignorée : demande inconnue {}", participant, requestId);
            return;
        }

        confirmations.record(requestId, participant, clock.instant());

        if (account.get().isPendingDeletion()
                && confirmations.findParticipants(requestId).containsAll(DeletionParticipant.ALL)) {
            userAccounts.markDeleted(requestId, clock.instant());
            log.info("Compte supprimé sur tous les services (requestId={})", requestId);
        }
    }
}
