package com.example.planeo_back.infrastructure.scheduler;

import com.example.planeo_back.domain.models.expense.ExpenseDomain;
import com.example.planeo_back.infrastructure.job.DeductExpenseAmountJobs;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class SchedulerService {

    private static final String JOB_GROUP = "expense-jobs";
    private static final String TRIGGER_GROUP = "expense-triggers";

    private final Scheduler scheduler;
    private final QuartzJobContextFactory quartzJobContextFactory;
    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    public SchedulerService(Scheduler scheduler, QuartzJobContextFactory quartzJobContextFactory) {
        this.scheduler = scheduler;
        this.quartzJobContextFactory = quartzJobContextFactory;
    }

    public void scheduleJob(ExpenseDomain expense, String username) throws SchedulerException {
        JobKey jobKey = jobKey(expense.id());
        TriggerKey triggerKey = triggerKey(expense.id());

        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .forJob(jobKey)
                .startAt(Date.from(resolveScheduledTime(expense.date())))
                .build();

        if (scheduler.checkExists(jobKey)) {
            scheduler.rescheduleJob(triggerKey, newTrigger);
            log.info("Replanification de la dépense {}", expense.id());
            return;
        }

        JobDataMap dataMap = quartzJobContextFactory
                .createJobDataMapWithUserContextAndExpenseId(expense.id(), username);
        JobDetail jobDetail = JobBuilder.newJob(DeductExpenseAmountJobs.class)
                .withIdentity(jobKey)
                .usingJobData(dataMap)
                .build();

        scheduler.scheduleJob(jobDetail, newTrigger);
        log.info("Nouvelle planification de la dépense {}", expense.id());
    }

    public void cancelJob(Long expenseId) throws SchedulerException {
        scheduler.deleteJob(jobKey(expenseId));
    }

    private JobKey jobKey(Long expenseId) {
        return JobKey.jobKey("expenseJob_" + expenseId, JOB_GROUP);
    }

    private TriggerKey triggerKey(Long expenseId) {
        return TriggerKey.triggerKey("expenseTrigger_" + expenseId, TRIGGER_GROUP);
    }

    private Instant resolveScheduledTime(LocalDate expenseDate) {
        ZoneId zone = ZoneId.of("Europe/Paris");
        LocalDate today = LocalDate.now(zone);

        if (!expenseDate.isAfter(today)) {
            return Instant.now().plus(3, ChronoUnit.SECONDS);
        }
        return expenseDate.atStartOfDay(zone).toInstant();
    }
}