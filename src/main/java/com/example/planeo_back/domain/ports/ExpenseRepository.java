package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.models.ExpenseAmountByCategoryDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.models.ExpensePerMonthDomain;
import com.example.planeo_back.domain.models.expense.ExpensesByCategoryDomain;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends IGenericCrudRepository<ExpenseDomain> {
    List<ExpenseDomain> findExpenseByUsername(String username);
    BigDecimal sumByUserIdAndStatus(@Param("username") String username, @Param("status") ExpenseStatus status);
    List<ExpensePerMonthDomain> getExpensePerMonthByUser(String username);
    ExpenseDomain update (ExpenseDomain expense);
    List<ExpenseAmountByCategoryDomain> getExpenseAmountByCategory(String username);
    List<ExpensesByCategoryDomain> getExpensesByCategory(String username);
    List<ExpenseDomain> findExpenseForCurrentMonth(String username);
    List<ExpenseDomain> findExpenseForLastMonths(String username, int monthCount);
    List<ExpenseDomain> findExpenseByUsernameAndStatus(String username, ExpenseStatus status);
}
