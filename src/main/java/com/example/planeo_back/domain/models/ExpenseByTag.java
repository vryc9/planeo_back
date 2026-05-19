package com.example.planeo_back.domain.models;

import com.example.planeo_back.domain.enums.Tag;

import java.math.BigDecimal;

public record ExpenseByTag(Tag tag, BigDecimal total) {
}
