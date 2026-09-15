package com.example.planeo_back.application.exception.account;

public enum AccountMessage {
    AMOUNT_SUM_MISMATCH("La somme des montants des comptes doit être égale au solde courant"),
    ACCOUNT_NOT_OWNED("Ce compte n'appartient pas à l'utilisateur");

    private final String label;

    AccountMessage(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
