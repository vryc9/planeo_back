package com.example.planeo_back.infrastructure.mapper;

import com.example.planeo_back.domain.models.balance.BalanceDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Balance;
import com.example.planeo_back.web.DTO.balance.BalanceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {
    BalanceDomain fromEntityToDomain(Balance balance);
    BalanceDomain fromDtoToDomain(BalanceDTO dto);
    Balance toEntity(BalanceDomain balanceDomain);
}
