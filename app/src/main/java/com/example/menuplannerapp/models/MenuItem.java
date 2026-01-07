package com.example.menuplannerapp.models;

public class MenuItem {
    private int id;
    private String date;        // Format: "2026-01-13"
    private String mealType;    // "breakfast", "lunch", "dinner"
    private Recipe recipe;

    // Constructeur vide
    public MenuItem() {
    }

    // Constructeur avec paramètres
    public MenuItem(String date, String mealType, Recipe recipe) {
        this.date = date;
        this.mealType = mealType;
        this.recipe = recipe;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMealType() {
        return mealType;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    // Méthode utilitaire pour obtenir le nom du repas en français
    public String getMealTypeName() {
        switch (mealType) {
            case "breakfast":
                return "Petit-déjeuner";
            case "lunch":
                return "Déjeuner";
            case "dinner":
                return "Dîner";
            default:
                return mealType;
        }
    }

    // Méthode pour obtenir le jour de la semaine
    public String getDayOfWeek() {
        // Cette méthode sera utile pour l'affichage
        // Pour l'instant on retourne juste la date
        return date;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "date='" + date + '\'' +
                ", mealType='" + mealType + '\'' +
                ", recipe=" + (recipe != null ? recipe.getName() : "null") +
                '}';
    }
}