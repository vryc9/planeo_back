package com.example.planeo_back.application.usecase.accountdeletion;

import com.example.planeo_back.domain.enums.DeletionParticipant;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.models.useraccount.UserAccountDomain;
import com.example.planeo_back.domain.ports.ExpenseRepository;
import com.example.planeo_back.domain.ports.ExpenseSchedulerPort;
import com.example.planeo_back.domain.ports.FinancialDataPurgePort;
import com.example.planeo_back.domain.ports.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * planeo_back's share of the account deletion. Idempotent: replaying the event after the
 * data is gone only re-records the (already present) confirmation.
 */
@Service
public class PurgeUserFinancialDataUseCase {

    private static final Logger log = LoggerFactory.getLogger(PurgeUserFinancialDataUseCase.class);

    private final UserAccountRepository userAccounts;
    private final ExpenseRepository expenses;
    private final ExpenseSchedulerPort scheduler;
    private final FinancialDataPurgePort purgePort;
    private final ConfirmAccountDeletionUseCase confirmAccountDeletion;

    public PurgeUserFinancialDataUseCase(UserAccountRepository userAccounts,
                                         ExpenseRepository expenses,
                                         ExpenseSchedulerPort scheduler,
                                         FinancialDataPurgePort purgePort,
                                         ConfirmAccountDeletionUseCase confirmAccountDeletion) {
        this.userAccounts = userAccounts;
        this.expenses = expenses;
        this.scheduler = scheduler;
        this.purgePort = purgePort;
        this.confirmAccountDeletion = confirmAccountDeletion;
    }

    @Transactional
    public void execute(String requestId, String username) {
        Optional<UserAccountDomain> account = userAccounts.findByIdForUpdate(requestId);

        // Only purge for a request that is really in flight for this very username. This also
        // protects data of a user who re-registered the same username after a completed deletion.
        if (account.isPresent() && account.get().isPendingDeletion() && username.equals(account.get().username())) {
            expenses.findExpenseByUsername(username).stream()
                    .map(ExpenseDomain::id)
                    .forEach(scheduler::cancel);
            purgePort.purge(username);
            log.info("Données financières purgées (requestId={})", requestId);
        }

        confirmAccountDeletion.execute(requestId, DeletionParticipant.BACK);
    }
}
