package com.example.menuplannerapp.logic;


import com.example.menuplannerapp.models.MenuItem;
import com.example.menuplannerapp.models.Recipe;

import java.util.ArrayList;
import java.util.Random;

public class MenuGenerator {

    private static final String[] DAYS = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"};
    private static final String[] MEALS = {"Petit-déjeuner","Déjeuner","Dîner"};

    public static ArrayList<MenuItem> generateWeeklyMenu(ArrayList<Recipe> recipes) {
        ArrayList<MenuItem> menu = new ArrayList<>();
        Random random = new Random();

        String[] mealTypes = {"breakfast","lunch","dinner"}; // correspond à ce que MenuItem attend

        for (int day = 1; day <= 7; day++) {
            for (String meal : mealTypes) {
                Recipe r = recipes.get(random.nextInt(recipes.size()));
                menu.add(new MenuItem("2026-01-" + String.format("%02d", day), meal, r));
            }
        }
        return menu;
    }

}

