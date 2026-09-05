package com.example.planeo_back.infrastructure.adapter.repository.marketprice;

import com.example.planeo_back.infrastructure.adapter.repository.entity.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMarketPriceRepository extends JpaRepository<MarketPrice,Long> {
}
