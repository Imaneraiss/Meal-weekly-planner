package com.example.menuplannerapp.models;

public class ShoppingItem {
    private int id;
    private String ingredient;
    private String totalQuantity;
    private boolean isPurchased;

    // Constructeur vide
    public ShoppingItem() {
        this.isPurchased = false;
    }

    // Constructeur avec paramètres
    public ShoppingItem(String ingredient, String totalQuantity) {
        this.ingredient = ingredient;
        this.totalQuantity = totalQuantity;
        this.isPurchased = false;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    // Méthode pour basculer l'état d'achat
    public void togglePurchased() {
        this.isPurchased = !this.isPurchased;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "ingredient='" + ingredient + '\'' +
                ", totalQuantity='" + totalQuantity + '\'' +
                ", isPurchased=" + isPurchased +
                '}';
    }
}