package com.example.planeo_back.infrastructure.adapter.scheduler;

import com.example.planeo_back.application.exception.scheduler.ExpenseSchedulerException;
import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.domain.ports.ExpenseSchedulerPort;
import com.example.planeo_back.infrastructure.scheduler.SchedulerService;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

@Component
public class ExpenseSchedulerAdapter implements ExpenseSchedulerPort {

    private final SchedulerService service;

    public ExpenseSchedulerAdapter(SchedulerService service) {
        this.service = service;
    }

    @Override
    public void schedule(ExpenseDomain expense, String username) {
        try {
            service.scheduleJob(expense, username);
        } catch (SchedulerException e) {
            throw new ExpenseSchedulerException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void cancel(Long id) {
        try {
            service.cancelJob(id);
        } catch (SchedulerException e) {
            throw new ExpenseSchedulerException("Échec d'annulation pour la dépense " + id, e);
        }
    }
}
