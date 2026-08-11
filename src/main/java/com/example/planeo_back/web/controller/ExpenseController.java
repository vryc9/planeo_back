package com.example.planeo_back.web.controller;

import com.example.planeo_back.application.service.expense.ExpenseService;
import com.example.planeo_back.web.DTO.ExpenseDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseAmountByCategoryDTO;
import com.example.planeo_back.web.DTO.expense.ExpenseCreateRequestDTO;
import com.example.planeo_back.web.DTO.expense.ExpensePerMonthDTO;
import com.example.planeo_back.web.DTO.expense.ExpensesByCategoryDTO;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping("/amount/category")
    public ResponseEntity<List<ExpenseAmountByCategoryDTO>> expensesAmountByCategory() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getExpenseAmountByCategory());
    }

    @GetMapping("/month")
    public ResponseEntity<List<ExpensePerMonthDTO>> month() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.getExpensePerMonths());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/month/number")
    public ResponseEntity<List<ExpenseDTO>> getExpensesForLastMonths(@RequestParam int monthCount) {
        return ResponseEntity.ok(service.getExpensesForLastMonths(monthCount));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpensesForCurrentMonth() {
        return ResponseEntity.ok(service.getExpensesForCurrentMonth());
    }

    @GetMapping("/category")
    public ResponseEntity<List<ExpensesByCategoryDTO>> getExpensesByCategory() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getExpensesByCategory());
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> create(@RequestBody ExpenseCreateRequestDTO expenseDTO) throws SchedulerException, IllegalAccessException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(expenseDTO));
    }

    @PutMapping
    public ResponseEntity<ExpenseDTO> update(@RequestBody ExpenseDTO expenseDTO) throws SchedulerException, IllegalAccessException {
        return ResponseEntity.ok(service.update(expenseDTO));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody ExpenseDTO expenseDTO) {
        service.delete(expenseDTO);
        return ResponseEntity.ok().build();
    }
}
