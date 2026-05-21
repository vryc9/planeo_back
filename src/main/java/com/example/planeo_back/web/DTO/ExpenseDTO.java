package com.example.planeo_back.web.DTO;

import com.example.planeo_back.domain.enums.ExpenseStatus;
import com.example.planeo_back.domain.enums.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record ExpenseDTO (
    Long id,
    BigDecimal amount,
    Tag tag,
    ExpenseStatus status,
    LocalDate date,
    String label,
    boolean recurring
){}

