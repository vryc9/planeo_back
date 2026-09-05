package com.example.planeo_back.domain.models.expense;

import java.time.YearMonth;
import java.util.List;

public record MonthlyExpensesByCategoryDomain(YearMonth month, List<ExpensesByCategoryDomain> categories) {
}
