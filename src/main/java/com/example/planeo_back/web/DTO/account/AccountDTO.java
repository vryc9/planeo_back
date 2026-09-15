package com.example.planeo_back.web.DTO.account;

import java.math.BigDecimal;

public record AccountDTO(Long id, String label, BigDecimal amount, String logo) {
}
