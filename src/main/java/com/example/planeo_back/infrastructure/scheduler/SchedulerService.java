package com.example.planeo_back.infrastructure.scheduler;

import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.infrastructure.job.DeductExpenseAmountJobs;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class SchedulerService {

    private final Scheduler scheduler;
    private final QuartzJobContextFactory quartzJobContextFactory;

    public SchedulerService(Scheduler scheduler, QuartzJobContextFactory quartzJobContextFactory) {
        this.scheduler = scheduler;
        this.quartzJobContextFactory = quartzJobContextFactory;
    }

    public void scheduleJob(ExpenseDomain expense, String username) throws SchedulerException {
        JobDataMap dataMap = quartzJobContextFactory.createJobDataMapWithUserContextAndExpenseId(
                expense.id(), username);

        JobDetail jobDetail = JobBuilder.newJob(DeductExpenseAmountJobs.class)
                .withIdentity("expenseJob_" + expense.id(), "expense-jobs")
                .usingJobData(dataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("expenseTrigger_" + expense.id(), "expense-triggers")
                .startAt(Date.from(resolveScheduledTime(expense.date())))
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private Instant resolveScheduledTime(LocalDate expenseDate) {
        var zone = ZoneId.of("Europe/Paris");
        var today = LocalDate.now(zone);

        if (!expenseDate.isAfter(today)) {
            return Instant.now().plus(3, ChronoUnit.SECONDS);
        }
        return expenseDate.atStartOfDay(zone).toInstant();
    }
}
