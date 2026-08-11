package com.example.planeo_back.infrastructure.adapter.repository.projection;

import java.math.BigDecimal;

public record CategoryAmountProjection(
        Long categoryId,
        String categoryName,
        String categoryIcon,
        BigDecimal totalAmount
) {}
