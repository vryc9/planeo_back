package com.example.planeo_back.infrastructure.mapper;

import com.example.planeo_back.domain.models.account.AccountDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Account;
import com.example.planeo_back.web.DTO.account.AccountDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "amount", source = "montant")
    AccountDomain fromEntityToDomain(Account account);

    @Mapping(target = "montant", source = "amount")
    Account toEntity(AccountDomain domain);

    AccountDTO toDTO(AccountDomain domain);

    List<AccountDTO> toDTO(List<AccountDomain> domains);
}
