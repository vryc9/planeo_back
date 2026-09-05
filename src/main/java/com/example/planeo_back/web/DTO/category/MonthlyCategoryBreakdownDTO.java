package com.example.planeo_back.web.DTO.category;

import com.example.planeo_back.web.DTO.expense.ExpenseAmountByCategoryDTO;

import java.time.YearMonth;
import java.util.List;

public record MonthlyCategoryBreakdownDTO(YearMonth month, List<ExpenseAmountByCategoryDTO> categories) {
}
