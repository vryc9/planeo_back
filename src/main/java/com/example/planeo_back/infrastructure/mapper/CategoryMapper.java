package com.example.planeo_back.infrastructure.mapper;

import com.example.planeo_back.domain.models.category.CategoryDomain;
import com.example.planeo_back.infrastructure.adapter.repository.entity.Category;
import com.example.planeo_back.web.DTO.category.CategoryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryDomain domain);
    CategoryDomain toDomain(Category category);
    CategoryDTO toDTO(CategoryDomain domain);
}
