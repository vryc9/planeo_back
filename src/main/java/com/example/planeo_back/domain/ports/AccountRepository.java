package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.models.account.AccountDomain;

import java.util.List;

public interface AccountRepository extends IGenericCrudRepository<AccountDomain> {
    List<AccountDomain> findByUsername(String username);
    boolean accountsExistForUser(String username);
    AccountDomain update(AccountDomain domain);
}
