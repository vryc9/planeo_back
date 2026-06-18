package com.example.planeo_back.web.DTO.expense;

import com.example.planeo_back.domain.enums.Tag;

import java.math.BigDecimal;

public record ExpenseAmountByTagDTO(Tag tag, BigDecimal total) {
}
