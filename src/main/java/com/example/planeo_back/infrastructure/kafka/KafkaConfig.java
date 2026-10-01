package com.example.planeo_back.infrastructure.kafka;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.backoff.ExponentialBackOff;

/**
 * Failed records are retried with exponential backoff, then parked in "<topic>.DLT"
 * so a poison message never blocks the partition. Producer and consumer factories come from
 * the spring.kafka.* properties.
 */
@Configuration
@EnableScheduling
public class KafkaConfig {

    @Bean
    public CommonErrorHandler kafkaErrorHandler(KafkaOperations<String, String> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
                (record, ex) -> new TopicPartition(record.topic() + ".DLT", -1));

        ExponentialBackOff backOff = new ExponentialBackOff(1_000L, 2.0);
        backOff.setMaxElapsedTime(60_000L);

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);
        // A malformed payload will never become valid: park it right away.
        handler.addNotRetryableExceptions(InvalidMessageException.class);
        return handler;
    }
}
