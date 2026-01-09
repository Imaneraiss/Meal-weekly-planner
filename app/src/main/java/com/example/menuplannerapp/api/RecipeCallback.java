package com.example.menuplannerapp.api;

import com.example.menuplannerapp.models.Recipe;
import java.util.ArrayList;

public interface RecipeCallback {
    void onSuccess(ArrayList<Recipe> recipes);
    void onError(String errorMessage);
}