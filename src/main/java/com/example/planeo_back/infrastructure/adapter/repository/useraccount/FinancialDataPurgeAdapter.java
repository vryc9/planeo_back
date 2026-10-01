package com.example.planeo_back.infrastructure.adapter.repository.useraccount;

import com.example.planeo_back.domain.ports.FinancialDataPurgePort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

/**
 * Bulk deletes, ordered so foreign keys are respected (expenses reference accounts and
 * categories). Each statement is idempotent.
 */
@Component
public class FinancialDataPurgeAdapter implements FinancialDataPurgePort {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void purge(String username) {
        delete("DELETE FROM Expense e WHERE e.username = :username", username);
        delete("DELETE FROM Account a WHERE a.username = :username", username);
        delete("DELETE FROM Balance b WHERE b.username = :username", username);
        delete("DELETE FROM Category c WHERE c.owner = :username", username);
        em.clear();
    }

    private void delete(String jpql, String username) {
        em.createQuery(jpql).setParameter("username", username).executeUpdate();
    }
}
