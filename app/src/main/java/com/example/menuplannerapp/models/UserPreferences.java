package com.example.menuplannerapp.models;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isGlutenFree;
    private boolean isDairyFree;
    private List<String> favoriteCategories;

    // Constructeur vide
    public UserPreferences() {
        this.isVegetarian = false;
        this.isVegan = false;
        this.isGlutenFree = false;
        this.isDairyFree = false;
        this.favoriteCategories = new ArrayList<>();
    }

    // Getters
    public boolean isVegetarian() {
        return isVegetarian;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public boolean isDairyFree() {
        return isDairyFree;
    }

    public List<String> getFavoriteCategories() {
        return favoriteCategories;
    }

    // Setters
    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public void setVegan(boolean vegan) {
        isVegan = vegan;
    }

    public void setGlutenFree(boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public void setDairyFree(boolean dairyFree) {
        isDairyFree = dairyFree;
    }

    public void setFavoriteCategories(List<String> favoriteCategories) {
        this.favoriteCategories = favoriteCategories;
    }

    public void addFavoriteCategory(String category) {
        if (!favoriteCategories.contains(category)) {
            favoriteCategories.add(category);
        }
    }

    public void removeFavoriteCategory(String category) {
        favoriteCategories.remove(category);
    }

    public boolean hasAnyRestriction() {
        return isVegetarian || isVegan || isGlutenFree || isDairyFree;
    }

    @Override
    public String toString() {
        return "UserPreferences{" +
                "isVegetarian=" + isVegetarian +
                ", isVegan=" + isVegan +
                ", isGlutenFree=" + isGlutenFree +
                ", isDairyFree=" + isDairyFree +
                ", favoriteCategories=" + favoriteCategories +
                '}';
    }
}