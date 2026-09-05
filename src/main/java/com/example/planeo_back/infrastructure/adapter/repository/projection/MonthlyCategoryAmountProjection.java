package com.example.planeo_back.infrastructure.adapter.repository.projection;

import java.math.BigDecimal;

public record MonthlyCategoryAmountProjection(int year,
                                              int month,
                                              Long categoryId,
                                              String categoryName,
                                              String categoryIcon,
                                              BigDecimal total
) {
}
