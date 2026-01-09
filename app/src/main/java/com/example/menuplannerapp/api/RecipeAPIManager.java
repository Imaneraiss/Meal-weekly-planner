package com.example.menuplannerapp.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.menuplannerapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeAPIManager {

    private static final String TAG = "RecipeAPIManager";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String FILTER_BY_CATEGORY = "filter.php?c=";
    private static final String LOOKUP_BY_ID = "lookup.php?i=";

    // Cache simple
    private ArrayList<Recipe> cachedRecipes = null;
    private String lastCategory = null;

    private RequestQueue requestQueue;

    public RecipeAPIManager(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Récupère les recettes selon les préférences utilisateur
     */
    public void fetchRecipes(Context context, RecipeCallback callback) {
        Log.d(TAG, "fetchRecipes() appelé");

        // Lire les préférences
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean isVegetarian = prefs.getBoolean("vegetarian", false);
        boolean isGlutenFree = prefs.getBoolean("gluten_free", false);
        boolean isHalal = prefs.getBoolean("halal", false);

        // Déterminer la catégorie
        String category = determineCategoryFromPreferences(isVegetarian, isGlutenFree, isHalal);
        Log.d(TAG, "Catégorie sélectionnée : " + category);

        // Vérifier le cache
        if (cachedRecipes != null && category.equals(lastCategory)) {
            Log.d(TAG, "Utilisation du cache : " + cachedRecipes.size() + " recettes");
            callback.onSuccess(cachedRecipes);
            return;
        }

        // Construire l'URL
        String url = BASE_URL + FILTER_BY_CATEGORY + category;
        Log.d(TAG, "URL : " + url);

        // Faire la requête HTTP
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Réponse reçue");
                        parseAndFetchDetails(response, callback, category);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Erreur : " + error.toString());
                        handleError(error, callback);
                    }
                }
        );

        requestQueue.add(request);
    }

    /**
     * Détermine la catégorie selon les préférences
     */
    private String determineCategoryFromPreferences(boolean isVegetarian, boolean isGlutenFree, boolean isHalal) {
        if (isVegetarian) {
            return "Vegetarian";
        }

        if (isGlutenFree || isHalal) {
            return "Seafood";
        }

        return Math.random() > 0.5 ? "Chicken" : "Seafood";
    }

    /**
     * Parse la réponse et récupère les détails de chaque recette
     */
    private void parseAndFetchDetails(JSONObject response, RecipeCallback callback, String category) {
        try {
            JSONArray mealsArray = response.getJSONArray("meals");
            int totalMeals = mealsArray.length();

            Log.d(TAG, "Recettes trouvées : " + totalMeals);

            if (totalMeals == 0) {
                callback.onError("Aucune recette trouvée");
                return;
            }

            // Limiter à 30 recettes max
            int mealsToFetch = Math.min(totalMeals, 30);
            ArrayList<Recipe> recipes = new ArrayList<>();
            final int[] fetchedCount = {0};

            // Récupérer les détails de chaque recette
            for (int i = 0; i < mealsToFetch; i++) {
                JSONObject mealObject = mealsArray.getJSONObject(i);
                String mealId = mealObject.getString("idMeal");

                fetchRecipeDetails(mealId, new RecipeCallback() {
                    @Override
                    public void onSuccess(ArrayList<Recipe> detailedRecipes) {
                        if (!detailedRecipes.isEmpty()) {
                            recipes.add(detailedRecipes.get(0));
                        }

                        fetchedCount[0]++;

                        // Quand toutes les recettes sont récupérées
                        if (fetchedCount[0] == mealsToFetch) {
                            Log.d(TAG, "Toutes les recettes récupérées : " + recipes.size());
                            cachedRecipes = recipes;
                            lastCategory = category;
                            callback.onSuccess(recipes);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        fetchedCount[0]++;

                        if (fetchedCount[0] == mealsToFetch) {
                            if (recipes.isEmpty()) {
                                callback.onError("Impossible de récupérer les détails");
                            } else {
                                cachedRecipes = recipes;
                                lastCategory = category;
                                callback.onSuccess(recipes);
                            }
                        }
                    }
                });
            }

        } catch (JSONException e) {
            Log.e(TAG, "Erreur JSON : " + e.getMessage());
            callback.onError("Erreur d'analyse des données : " + e.getMessage());
        }
    }

    /**
     * Récupère les détails d'une recette par ID
     */
    private void fetchRecipeDetails(String mealId, RecipeCallback callback) {
        String url = BASE_URL + LOOKUP_BY_ID + mealId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray mealsArray = response.getJSONArray("meals");
                            if (mealsArray.length() > 0) {
                                JSONObject mealObject = mealsArray.getJSONObject(0);
                                Recipe recipe = parseRecipeFromJSON(mealObject);

                                ArrayList<Recipe> recipes = new ArrayList<>();
                                recipes.add(recipe);
                                callback.onSuccess(recipes);
                            } else {
                                callback.onError("Recette non trouvée");
                            }
                        } catch (JSONException e) {
                            callback.onError("Erreur parsing : " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Erreur réseau : " + error.toString());
                    }
                }
        );

        requestQueue.add(request);
    }

    /**
     * Parse un objet JSON en objet Recipe
     */
    private Recipe parseRecipeFromJSON(JSONObject mealObject) throws JSONException {
        Recipe recipe = new Recipe();

        recipe.setId(mealObject.getString("idMeal"));
        recipe.setName(mealObject.getString("strMeal"));
        recipe.setCategory(mealObject.getString("strCategory"));
        recipe.setArea(mealObject.getString("strArea"));
        recipe.setInstructions(mealObject.getString("strInstructions"));
        recipe.setThumbnailUrl(mealObject.getString("strMealThumb"));

        // Parser les ingrédients (strIngredient1...strIngredient20)
        for (int i = 1; i <= 20; i++) {
            String ingredientKey = "strIngredient" + i;
            String measureKey = "strMeasure" + i;

            String ingredient = mealObject.optString(ingredientKey, "");
            String measure = mealObject.optString(measureKey, "");

            if (ingredient != null && !ingredient.trim().isEmpty()) {
                recipe.addIngredient(ingredient.trim(), measure.trim());
            }
        }

        Log.d(TAG, "Recette parsée : " + recipe.getName() + " avec " + recipe.getIngredients().size() + " ingrédients");

        return recipe;
    }

    /**
     * Gère les erreurs Volley
     */
    private void handleError(VolleyError error, RecipeCallback callback) {
        String errorMessage;

        if (error.networkResponse == null) {
            errorMessage = "Pas de connexion Internet. Vérifiez votre connexion.";
        } else {
            int statusCode = error.networkResponse.statusCode;
            switch (statusCode) {
                case 404:
                    errorMessage = "Ressource non trouvée (404)";
                    break;
                case 500:
                    errorMessage = "Erreur serveur (500)";
                    break;
                default:
                    errorMessage = "Erreur réseau (Code: " + statusCode + ")";
            }
        }

        Log.e(TAG, "Erreur : " + errorMessage);
        callback.onError(errorMessage);
    }

    // Méthodes utilitaires pour le cache
    public void clearCache() {
        cachedRecipes = null;
        lastCategory = null;
        Log.d(TAG, "Cache vidé");
    }

    public boolean hasCache() {
        return cachedRecipes != null && !cachedRecipes.isEmpty();
    }

    public ArrayList<Recipe> getCachedRecipes() {
        return cachedRecipes;
    }
}