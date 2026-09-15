package com.example.planeo_back.infrastructure.adapter.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String label;

    @Column(name = "montant", nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(name = "logo", columnDefinition = "MEDIUMTEXT")
    private String logo;

    public Account() {
    }

    public Account(String username, String label, BigDecimal montant, String logo) {
        this.username = username;
        this.label = label;
        this.montant = montant;
        this.logo = logo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
