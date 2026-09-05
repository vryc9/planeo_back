package com.example.planeo_back.web.DTO.category;

import java.math.BigDecimal;

public record CategoryAmountResponse(Long categoryId, String categoryName, String categoryIcon, BigDecimal total) {
}
