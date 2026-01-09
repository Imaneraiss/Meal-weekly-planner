package com.example.menuplannerapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.menuplannerapp.models.UserPreferences;

public class PreferencesManager {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_VEGETARIAN = "vegetarian";
    private static final String KEY_GLUTEN_FREE = "gluten_free";
    private static final String KEY_HALAL = "halal";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    /**
     * Récupère les préférences utilisateur
     */
    public static UserPreferences getPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setVegetarian(prefs.getBoolean(KEY_VEGETARIAN, false));
        userPreferences.setGlutenFree(prefs.getBoolean(KEY_GLUTEN_FREE, false));
        userPreferences.setDairyFree(false);

        return userPreferences;
    }

    /**
     * Sauvegarde les préférences
     */
    public static void savePreferences(Context context, UserPreferences preferences) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(KEY_VEGETARIAN, preferences.isVegetarian());
        editor.putBoolean(KEY_GLUTEN_FREE, preferences.isGlutenFree());

        editor.apply();
    }

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public static void setNotFirstLaunch(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    public static void clearAllPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}