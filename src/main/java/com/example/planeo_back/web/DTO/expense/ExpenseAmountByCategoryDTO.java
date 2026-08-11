package com.example.planeo_back.web.DTO.expense;

import com.example.planeo_back.web.DTO.category.CategoryDTO;
import java.math.BigDecimal;

public record ExpenseAmountByCategoryDTO(CategoryDTO category, BigDecimal total) {
}
