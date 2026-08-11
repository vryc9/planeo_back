package com.example.planeo_back.domain.models.category;

public record CategoryDomain(Long id, String name, String icon, String owner) {
    public static CategoryDomain build(String name, String icon, String owner) {
        return new CategoryDomain(null, name, icon, owner);
    }
    public static CategoryDomain buildWithId(Long id, String name, String icon, String owner) {
        return new CategoryDomain(id, name, icon, owner);
    }
}
