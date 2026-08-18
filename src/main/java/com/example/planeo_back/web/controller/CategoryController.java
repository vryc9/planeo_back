package com.example.planeo_back.web.controller;

import com.example.planeo_back.application.exception.category.CategoryMessage;
import com.example.planeo_back.application.service.category.CategoryService;
import com.example.planeo_back.web.DTO.category.CategoryCreateRequestDTO;
import com.example.planeo_back.web.DTO.category.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryCreateRequestDTO createRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(createRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategoriesByUser() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCategoriesByUsers());
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody CategoryDTO categoryDTO) {
        service.delete(categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
