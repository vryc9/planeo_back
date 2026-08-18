package com.example.planeo_back.application.exception.category;

public enum CategoryMessage {
    EMPTY_ID("L'id doit être présent"),
    CATEGORY_IS_USED("La catégorie ne peut pas être supprimée car elle est utilisée par des dépenses");

    private final String label;
    
    CategoryMessage(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
