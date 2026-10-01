package com.example.planeo_back.domain.ports;

public interface FinancialDataPurgePort {
    /** Irreversibly removes every expense, account, balance and custom category of the user. */
    void purge(String username);
}
