package com.example.planeo_back.web.DTO.category;

import jakarta.validation.constraints.NotBlank;

public record  CategoryCreateRequestDTO(
        @NotBlank(message = "Le nom de la catégorie est obligatoire")
        String name,

        @NotBlank(message = "L'icone ne doit pas être vide")
        String icon) {
}
