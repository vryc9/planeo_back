package com.example.planeo_back.infrastructure.adapter.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "market_price")
public class MarketPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 12, scale = 4)
    private BigDecimal price;

    @Column(name = "previous_close", nullable = false, precision = 12, scale = 4, unique = true)
    BigDecimal previousClose;

    @Column(nullable = false)
    String currency;

    @Column(nullable = false)
    Instant fetchedAt;

    public MarketPrice() {
    }

    public MarketPrice(Long id, String name, BigDecimal price, BigDecimal previousClose, String currency, Instant fetchedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.previousClose = previousClose;
        this.currency = currency;
        this.fetchedAt = fetchedAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(BigDecimal previousClose) {
        this.previousClose = previousClose;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(Instant fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}

