package com.example.planeo_back.infrastructure.mapper;

import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.models.expense.ExpensesByTagDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.models.ExpenseAmountByTagDomain;
import com.example.planeo_back.domain.models.ExpensePerMount;
import com.example.planeo_back.web.DTO.ExpenseDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseAmountByTagDTO;
import com.example.planeo_back.web.DTO.expense.ExpensePerMonthDTO;
import com.example.planeo_back.web.DTO.expense.ExpensesByTagsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    ExpenseDomain fromEntityToDomain(Expense expense);
    ExpenseDomain fromDtoToDomain(ExpenseDTO expenseDTO);
    ExpenseDTO fromDomainToDTO(ExpenseDomain domain);
    ExpenseDTO toDTO(Expense expense);
    List<ExpenseDTO> toDTO(List<Expense> expenses);
    List<ExpensePerMonthDTO> transformExpensePerMonthDTO(List<ExpensePerMount> expensePerMounts);
    List<ExpenseAmountByTagDTO> transformExpenseAmountByTags(List<ExpenseAmountByTagDomain> expenseAmountByTagDomains);
    List<ExpensesByTagsDTO> transformExpensesTagsToDTO(List<ExpensesByTagDomain> expensesByTags);
    Expense toEntity(ExpenseDomain expenseDomain);
    List<Expense> toEntity(List<ExpenseDTO> expenseDTOs);
}
