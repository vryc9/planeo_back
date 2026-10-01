package com.example.planeo_back.infrastructure.kafka;

import com.example.planeo_back.infrastructure.adapter.repository.entity.OutboxEvent;
import com.example.planeo_back.infrastructure.adapter.repository.outbox.JpaOutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Ships outbox rows to Kafka. Delivery is at-least-once (a crash between send and commit
 * re-sends the row), which is fine because every consumer is idempotent.
 */
@Component
public class OutboxRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelay.class);

    private final JpaOutboxEventRepository outbox;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Clock clock;
    private final int batchSize;
    private final Duration retention;

    public OutboxRelay(JpaOutboxEventRepository outbox,
                       KafkaTemplate<String, String> kafkaTemplate,
                       Clock clock,
                       @Value("${planeo.outbox.batch-size}") int batchSize,
                       @Value("${planeo.outbox.retention-days}") long retentionDays) {
        this.outbox = outbox;
        this.kafkaTemplate = kafkaTemplate;
        this.clock = clock;
        this.batchSize = batchSize;
        this.retention = Duration.ofDays(retentionDays);
    }

    @Scheduled(fixedDelayString = "${planeo.outbox.poll-interval-ms}")
    @Transactional
    public void relay() {
        List<OutboxEvent> pending = outbox.findPending(PageRequest.of(0, batchSize));
        for (OutboxEvent event : pending) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getEventKey(), event.getPayload()).get(10, TimeUnit.SECONDS);
                event.setPublishedAt(clock.instant());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception e) {
                // Stop the batch to keep ordering; the row stays pending and is retried next tick.
                log.warn("Publication outbox {} impossible, nouvelle tentative au prochain cycle : {}",
                        event.getId(), e.getMessage());
                return;
            }
        }
    }

    @Scheduled(cron = "0 30 3 * * *")
    @Transactional
    public void purgePublished() {
        int deleted = outbox.deletePublishedBefore(clock.instant().minus(retention));
        if (deleted > 0) {
            log.info("{} évènements outbox publiés purgés", deleted);
        }
    }
}
