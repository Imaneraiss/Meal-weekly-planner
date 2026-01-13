package com.example.menuplannerapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private String id;
    private String name;
    private String category;
    private String area;
    private String instructions;
    private String thumbnailUrl;
    private List<String> ingredients;
    private List<String> measures;

    // Constructeur vide
    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.measures = new ArrayList<>();
    }

    // Constructeur avec paramètres
    public Recipe(String id, String name, String category, String area) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.ingredients = new ArrayList<>();
        this.measures = new ArrayList<>();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getMeasures() {
        return measures;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setMeasures(List<String> measures) {
        this.measures = measures;
    }

    public void addIngredient(String ingredient, String measure) {
        if (ingredient != null && !ingredient.trim().isEmpty()) {
            this.ingredients.add(ingredient);
            this.measures.add(measure != null ? measure : "");
        }
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}