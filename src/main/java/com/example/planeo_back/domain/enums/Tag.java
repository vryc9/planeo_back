package com.example.planeo_back.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Tag {

    SOIREE("Soirée"),
    RESTAURANT("Restaurant"),
    ANNIVERSAIRE("Anniversaire"),
    CINEMA("Cinéma");

    private final String label;

    Tag(String label) {
        this.label = label;
    }

    @JsonCreator
    public static Tag fromLabel(String value) {
        return Arrays.stream(values())
                .filter(t -> t.label.equals(value) || t.name().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown tag: " + value));
    }
}
