package com.example.planeo_back.domain.ports;
import com.example.planeo_back.domain.models.balance.BalanceDomain;

import java.math.BigDecimal;

public interface BalanceRepository extends IGenericCrudRepository<BalanceDomain> {
    BalanceDomain findBalanceByUsername(String username);
    void decreaseCurrentBalance(String username, BigDecimal amount);
    boolean balanceExistForUser(String username);
    BalanceDomain update(BalanceDomain domain);
}
