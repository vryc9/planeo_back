package com.example.planeo_back.infrastructure.adapter.investment.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YahooResponse(Chart chart) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Chart(List<Result> result, Object error) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(Meta meta) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Meta(
            String symbol,
            String currency,
            @JsonProperty("shortName") String shortName,
            @JsonProperty("regularMarketPrice") BigDecimal regularMarketPrice,
            @JsonProperty("previousClose") BigDecimal previousClose
    ) {}
}
