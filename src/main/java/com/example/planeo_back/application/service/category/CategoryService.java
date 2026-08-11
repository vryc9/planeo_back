package com.example.planeo_back.application.service.category;

import com.example.planeo_back.application.service.security.AuthService;
import com.example.planeo_back.domain.models.category.CategoryDomain;
import com.example.planeo_back.domain.ports.CategoryRepository;
import com.example.planeo_back.infrastructure.mapper.CategoryMapper;
import com.example.planeo_back.web.DTO.category.CategoryCreateRequestDTO;
import com.example.planeo_back.web.DTO.category.CategoryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;
    private final AuthService authService;

    public CategoryService(CategoryMapper mapper, CategoryRepository repository, AuthService authService) {
        this.mapper = mapper;
        this.repository = repository;
        this.authService = authService;
    }

    public CategoryDTO save(CategoryCreateRequestDTO categoryDTO) {
        return mapper.toDTO(repository.save(CategoryDomain.build(categoryDTO.name(), categoryDTO.icon(), authService.getUsername())));
    }

    public List<CategoryDTO> getCategoriesByUsers() {
        return repository.findVisibleForUser(authService.getUsername()).stream().map(mapper::toDTO).toList();
    }
}
