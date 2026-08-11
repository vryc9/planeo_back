package com.example.planeo_back.domain.ports;

import com.example.planeo_back.domain.models.category.CategoryDomain;

import java.util.List;

public interface CategoryRepository extends IGenericCrudRepository<CategoryDomain> {
    List<CategoryDomain> findVisibleForUser(String username);
}
