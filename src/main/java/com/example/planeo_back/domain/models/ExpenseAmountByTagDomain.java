package com.example.planeo_back.domain.models;

import com.example.planeo_back.domain.enums.Tag;

import java.math.BigDecimal;

public record ExpenseAmountByTagDomain(Tag tag, BigDecimal total) {
}
