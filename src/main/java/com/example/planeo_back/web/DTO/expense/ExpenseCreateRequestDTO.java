package com.example.planeo_back.web.DTO.expense;

import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.enums.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseCreateRequestDTO(
        BigDecimal amount,
        Tag tag,
        ExpenseStatus status,
        LocalDate date,
        String label,
        boolean recurring
){}
