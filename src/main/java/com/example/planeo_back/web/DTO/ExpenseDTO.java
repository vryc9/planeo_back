package com.example.planeo_back.web.DTO;

import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.web.DTO.category.CategoryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDTO (
    Long id,
    BigDecimal amount,
    CategoryDTO category,
    ExpenseStatus status,
    LocalDate date,
    String label,
    boolean recurring
){}

