package com.example.planeo_back.infrastructure.scheduler;

import com.example.planeo_back.domain.ports.MarkerPricePort;
import com.example.planeo_back.domain.ports.MarkerPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MarketPriceScheduler {
    private static final Logger log = LoggerFactory.getLogger(MarketPriceScheduler.class);

    private final MarkerPricePort port;
    private final MarkerPriceRepository repository;

    public MarketPriceScheduler(MarkerPricePort port, MarkerPriceRepository repository) {
        this.port = port;
        this.repository = repository;
    }

    @Scheduled(fixedRateString = "720000")
    public void refreshQuote() {
        port.fetch()
                .ifPresentOrElse(
                         repository::save,
                        () -> log.warn("Aucune cotation disponible")
                );
    }
}
