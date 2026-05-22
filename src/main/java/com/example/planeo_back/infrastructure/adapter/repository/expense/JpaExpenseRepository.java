package com.example.planeo_back.infrastructure.adapter.repository.expense;

import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.ExpenseByTag;
import com.example.planeo_back.domain.models.ExpensePerMount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JpaExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findExpenseByUsernameOrderByDateDesc(String username);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.username = :username AND e.status = :status")
    BigDecimal sumByUserIdAndStatus(@Param("username") String username, @Param("status") ExpenseStatus status);

    @Query("""
                SELECT new com.example.planeo_back.domain.models.ExpensePerMount(
                    MONTH(e.date), SUM(e.amount)
                )
                FROM Expense e
                WHERE e.username = :username
                GROUP BY MONTH(e.date)
                ORDER BY MONTH(e.date)
            """)
    List<ExpensePerMount> getExpensePerMountByUser(String username);

    @Query("""
        SELECT new com.example.planeo_back.domain.models.ExpenseByTag(
            e.tag,
            SUM(e.amount)
        )
        FROM Expense e
        WHERE e.username = :username
          AND YEAR(e.date) = YEAR(CURRENT_DATE)
          AND MONTH(e.date) = MONTH(CURRENT_DATE)
        GROUP BY e.tag
        ORDER BY SUM(e.amount) DESC
        """)
    List<ExpenseByTag> findTotalAmountByTagForCurrentMonth(@Param("username") String username);
}
