package com.example.planeo_back.domain.models.expense;

import com.example.planeo_back.domain.enums.Tag;

import java.util.List;

public record ExpensesByTagDomain(Tag tag, List<ExpenseDomain> expenses) {
}
