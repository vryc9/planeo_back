package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.models.investment.MarketPlaceDomain;

import java.util.Optional;

public interface MarkerPricePort {
    Optional<MarketPlaceDomain> fetch();
}
