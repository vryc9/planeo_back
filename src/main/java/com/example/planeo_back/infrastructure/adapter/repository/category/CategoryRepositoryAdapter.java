package com.example.planeo_back.infrastructure.adapter.repository.category;

import com.example.planeo_back.domain.models.category.CategoryDomain;
import com.example.planeo_back.domain.ports.CategoryRepository;
import com.example.planeo_back.infrastructure.mapper.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryAdapter implements CategoryRepository {
    private final JpaCategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryRepositoryAdapter(JpaCategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoryDomain save(CategoryDomain entity) {
        return mapper.toDomain(repository.save(mapper.toEntity(entity)));
    }

    @Override
    public Optional<CategoryDomain> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<CategoryDomain> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<CategoryDomain> findVisibleForUser(String username) {
        return repository.findVisibleForUser(username).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(CategoryDomain categoryDomain) {
        repository.deleteById(categoryDomain.id());
    }
}
