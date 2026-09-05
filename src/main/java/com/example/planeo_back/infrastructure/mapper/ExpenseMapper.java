package com.example.planeo_back.infrastructure.mapper;

import com.example.planeo_back.domain.models.ExpenseAmountByCategoryDomain;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.models.expense.ExpensesByCategoryDomain;
import com.example.planeo_back.domain.models.expense.MonthlyExpensesByCategoryDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Expense;
import com.example.planeo_back.domain.models.ExpensePerMonthDomain;
import com.example.planeo_back.web.DTO.ExpenseDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseAmountByCategoryDTO;
import com.example.planeo_back.web.DTO.expense.ExpensePerMonthDTO;
import com.example.planeo_back.web.DTO.expense.ExpensesByCategoryDTO;
import com.example.planeo_back.web.DTO.expense.MonthlyExpensesByCategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    ExpenseDomain fromEntityToDomain(Expense expense);
    ExpenseDomain fromDtoToDomain(ExpenseDTO expenseDTO);
    ExpenseDTO fromDomainToDTO(ExpenseDomain domain);
    ExpenseDTO toDTO(Expense expense);
    List<ExpenseDTO> toDTO(List<Expense> expenses);
    List<ExpensePerMonthDTO> transformExpensePerMonthDTO(List<ExpensePerMonthDomain> expensePerMonthDomains);
    List<ExpenseAmountByCategoryDTO> transformExpenseAmountByCategories(List<ExpenseAmountByCategoryDomain> expenseAmountByCategoryDomains);
    List<ExpensesByCategoryDTO> transformExpensesCategoryToDTO(List<ExpensesByCategoryDomain> expensesByCategory);
    List<MonthlyExpensesByCategoryDTO> transformMonthlyExpensesCategoryToDTO(List<MonthlyExpensesByCategoryDomain> expensesByCategory);
    Expense toEntity(ExpenseDomain expenseDomain);
    List<Expense> toEntity(List<ExpenseDTO> expenseDTOs);
}
