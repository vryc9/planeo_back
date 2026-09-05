package com.example.planeo_back.web.DTO.expense;

import java.time.YearMonth;
import java.util.List;

public record MonthlyExpensesByCategoryDTO (YearMonth month, List<ExpensesByCategoryDTO> categories){
}
