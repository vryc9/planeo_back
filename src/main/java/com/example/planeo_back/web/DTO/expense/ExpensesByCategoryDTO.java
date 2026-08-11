package com.example.planeo_back.web.DTO.expense;

import com.example.planeo_back.web.DTO.ExpenseDTO;
import com.example.planeo_back.web.DTO.category.CategoryDTO;

import java.util.List;

public record ExpensesByCategoryDTO(CategoryDTO category, List<ExpenseDTO> expenses) {
}
