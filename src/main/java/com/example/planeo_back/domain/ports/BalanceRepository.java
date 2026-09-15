package com.example.planeo_back.domain.ports;
import com.example.planeo_back.domain.models.balance.BalanceDomain;

public interface BalanceRepository extends IGenericCrudRepository<BalanceDomain> {
    BalanceDomain findBalanceByUsername(String username);
    boolean balanceExistForUser(String username);
}
