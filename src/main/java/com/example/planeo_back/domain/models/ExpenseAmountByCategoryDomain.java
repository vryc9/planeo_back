package com.example.planeo_back.domain.models;

import com.example.planeo_back.domain.models.category.CategoryDomain;

import java.math.BigDecimal;

public record ExpenseAmountByCategoryDomain(CategoryDomain category, BigDecimal total) {
    public static ExpenseAmountByCategoryDomain build(Long id, String name, String icon, String owner, BigDecimal total) {
        return new ExpenseAmountByCategoryDomain(CategoryDomain.buildWithId(id,name, icon, owner), total);
    }
}
