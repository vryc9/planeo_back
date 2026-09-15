package com.example.planeo_back.infrastructure.adapter.repository.account;

import com.example.planeo_back.infrastructure.adapter.repository.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUsername(String username);

    boolean existsByUsername(String username);
}
