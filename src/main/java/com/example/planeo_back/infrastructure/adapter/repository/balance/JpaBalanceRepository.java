package com.example.planeo_back.infrastructure.adapter.repository.balance;

import com.example.planeo_back.infrastructure.adapter.repository.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface JpaBalanceRepository extends JpaRepository<Balance, Long> {
    Balance findBalanceByUsername(String username);

    @Modifying
    @Query("UPDATE Balance b SET b.currentBalance = b.currentBalance - :amount WHERE b.username = :username")
    void decreaseCurrentBalance(String username, BigDecimal amount);

    boolean existsByUsername(String username);
}
