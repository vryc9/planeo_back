package com.example.planeo_back.domain.models.expense;

import com.example.planeo_back.domain.models.category.CategoryDomain;

import java.util.List;

public record ExpensesByCategoryDomain(CategoryDomain category, List<ExpenseDomain> expenses) {
}
