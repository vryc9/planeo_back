package com.example.planeo_back.application.exception.account;

public enum AccountMessage {
    AMOUNT_SUM_MISMATCH("La somme des montants des comptes doit être égale au solde courant"),
    ACCOUNT_NOT_OWNED("Ce compte n'appartient pas à l'utilisateur"),
    ACCOUNT_NOT_FOUND("Aucun compte ne correspond à cet id"),
    INVALID_PARAMETER("Le paramètre est invalide"),
    SAME_ACCOUNT_TRANSERT("Le transfert de solde de peut pas se faire sur un même compte"),
    INSUFFICIENT_BALANCE("Le solde est insuffisant");

    private final String label;

    AccountMessage(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
