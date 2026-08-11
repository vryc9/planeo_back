package com.example.planeo_back.infrastructure.adapter.repository.projection;

import java.math.BigDecimal;

public record ExpensePerMonthProjection(int month, BigDecimal total) {
}
