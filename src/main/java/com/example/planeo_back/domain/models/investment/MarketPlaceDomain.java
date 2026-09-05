package com.example.planeo_back.domain.models.investment;

import java.math.BigDecimal;
import java.time.Instant;

public record MarketPlaceDomain(
        Long id,
        String name,
        BigDecimal price,
        BigDecimal previousClose,
        String  currency,
        Instant fetchedAt
) {

    public static MarketPlaceDomain buildWithNoId(String name, BigDecimal price, BigDecimal previousClose, String currency, Instant fetchedAt) {
        return new MarketPlaceDomain(null,name, price, previousClose, currency, fetchedAt);
    }
    public static MarketPlaceDomain buildWithNoId(Long id, String name, BigDecimal price, BigDecimal previousClose, String currency, Instant fetchedAt) {
        return new MarketPlaceDomain(id,name, price, previousClose, currency, fetchedAt);
    }
}
