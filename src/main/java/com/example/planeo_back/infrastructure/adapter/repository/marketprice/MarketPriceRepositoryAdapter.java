package com.example.planeo_back.infrastructure.adapter.repository.marketprice;

import com.example.planeo_back.domain.models.investment.MarketPlaceDomain;
import com.example.planeo_back.domain.ports.MarkerPriceRepository;
import com.example.planeo_back.infrastructure.mapper.MarketPriceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MarketPriceRepositoryAdapter implements MarkerPriceRepository {
    private final MarketPriceMapper mapper;
    private final JpaMarketPriceRepository repository;

    public MarketPriceRepositoryAdapter(MarketPriceMapper mapper, JpaMarketPriceRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public MarketPlaceDomain save(MarketPlaceDomain entity) {
        return mapper.toDomain(repository.save(mapper.toEntity(entity)));
    }

    @Override
    public Optional<MarketPlaceDomain> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<MarketPlaceDomain> findAll() {
        return List.of();
    }

    @Override
    public void delete(MarketPlaceDomain marketPlaceDomain) {

    }
}
