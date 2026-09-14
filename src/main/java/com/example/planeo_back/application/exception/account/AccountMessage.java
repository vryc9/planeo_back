package com.example.planeo_back.application.exception.account;

public enum AccountMessage {
    AMOUNT_SUM_MISMATCH("La somme des montants des comptes doit être égale au solde courant");

    private final String label;

    AccountMessage(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
