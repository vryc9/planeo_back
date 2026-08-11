package com.example.planeo_back.infrastructure.adapter.repository.expense;

import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.infrastructure.adapter.repository.projection.CategoryAmountProjection;
import com.example.planeo_back.infrastructure.adapter.repository.projection.ExpensePerMonthProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JpaExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findExpenseByUsernameOrderByDateDesc(String username);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.username = :username AND e.status = :status")
    BigDecimal sumByUserIdAndStatus(@Param("username") String username, @Param("status") ExpenseStatus status);

    @Query("""
            SELECT new com.example.planeo_back.infrastructure.adapter.repository.projection.ExpensePerMonthProjection(
                MONTH(e.date), SUM(e.amount)
            )
            FROM Expense e
            WHERE e.username = :username
              AND YEAR(e.date) = YEAR(CURRENT_DATE)
            GROUP BY MONTH(e.date)
            ORDER BY MONTH(e.date)
        """)
    List<ExpensePerMonthProjection> getExpensePerMonthByUser(String username);

    @Query("""
        SELECT new com.example.planeo_back.infrastructure.adapter.repository.projection.CategoryAmountProjection(
            e.category.id, e.category.name, e.category.icon, SUM(e.amount)
        )
        FROM Expense e
        JOIN e.category
        WHERE e.username = :username
          AND YEAR(e.date) = YEAR(CURRENT_DATE)
          AND MONTH(e.date) = MONTH(CURRENT_DATE)
        GROUP BY e.category.id, e.category.name, e.category.icon
        ORDER BY SUM(e.amount) DESC
        """)
    List<CategoryAmountProjection> findTotalAmountByCategoryForCurrentMonth(@Param("username") String username);

    @Query("""
            SELECT e FROM Expense e
            JOIN FETCH e.category
            WHERE e.username = :username
              AND e.date >= :startDate
            ORDER BY e.date DESC
        """)
    List<Expense> findExpenseByUsernameSince(@Param("username") String username, @Param("startDate") LocalDate startDate);

    List<Expense> findExpenseByUsernameAndStatus(String username, ExpenseStatus status);
}
