package com.example.planeo_back.infrastructure.adapter.repository.balance;

import com.example.planeo_back.infrastructure.adapter.repository.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBalanceRepository extends JpaRepository<Balance, Long> {
    Balance findBalanceByUsername(String username);

    boolean existsByUsername(String username);
}
