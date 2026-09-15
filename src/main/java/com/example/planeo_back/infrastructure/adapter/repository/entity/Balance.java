package com.example.planeo_back.infrastructure.adapter.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(precision = 10, scale = 2)
    private BigDecimal currentBalance;

    @Column(nullable = false)
    private String username;

    public Balance() {
    }

    public Balance(BigDecimal currentBalance, String username) {
        this.currentBalance = currentBalance;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
