package com.example.planeo_back.web.DTO.expense;

import java.math.BigDecimal;

public record ExpensePerMonthDTO(Integer month, BigDecimal amount) {
}
