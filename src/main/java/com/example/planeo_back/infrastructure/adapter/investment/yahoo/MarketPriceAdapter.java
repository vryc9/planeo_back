package com.example.planeo_back.infrastructure.adapter.investment.yahoo;

import com.example.planeo_back.domain.models.investment.MarketPlaceDomain;
import com.example.planeo_back.domain.ports.MarkerPricePort;
import com.example.planeo_back.domain.ports.MarkerPriceRepository;
import com.example.planeo_back.infrastructure.adapter.investment.record.YahooResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.util.Optional;

@Component
public class MarketPriceAdapter implements MarkerPricePort {
    private final YahooFinanceClient yahooFinanceClient;
    private static final Logger log = LoggerFactory.getLogger(MarketPriceAdapter.class);

    public MarketPriceAdapter(YahooFinanceClient yahooFinanceClient) {
        this.yahooFinanceClient = yahooFinanceClient;
    }

    @Override
    public Optional<MarketPlaceDomain> fetch() {
        try {
            YahooResponse response = yahooFinanceClient.getChart("DCAM.PA", "1d", "1m");
            return Optional.ofNullable(response)
                    .map(YahooResponse::chart)
                    .flatMap(chart -> chart.result().stream().findFirst())
                    .map(YahooResponse.Result::meta)
                    .map(m -> MarketPlaceDomain.buildWithNoId(
                            m.shortName(), m.regularMarketPrice(), m.previousClose(), m.currency(), Instant.now()));
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Rate limit atteint sur l'API Yahoo Finance, cotation ignorée pour ce cycle");
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("Échec de récupération de la cotation marketplace", e);
            return Optional.empty();
        }
    }
}
