package com.example.planeo_back.infrastructure.mapper;

import com.example.planeo_back.domain.models.investment.MarketPlaceDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.MarketPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarketPriceMapper {
    MarketPlaceDomain toDomain(MarketPrice entity);
    MarketPrice toEntity(MarketPlaceDomain domain);
}
