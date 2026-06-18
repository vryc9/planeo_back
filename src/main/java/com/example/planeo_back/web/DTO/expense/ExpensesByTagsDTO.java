package com.example.planeo_back.web.DTO.expense;

import com.example.planeo_back.domain.enums.Tag;
import com.example.planeo_back.web.DTO.ExpenseDTO;

import java.util.List;

public record ExpensesByTagsDTO(Tag tag, List<ExpenseDTO> expenses) {
}
