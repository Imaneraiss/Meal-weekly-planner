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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RecipeAPIManager {

    private static final String TAG = "RecipeAPIManager";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String FILTER_BY_CATEGORY = "filter.php?c=";
    private static final String LOOKUP_BY_ID = "lookup.php?i=";

    // Ingrédients contenant du gluten
    private static final Set<String> GLUTEN_INGREDIENTS = new HashSet<>(Arrays.asList(
            "flour", "wheat", "bread", "pasta", "noodles", "soy sauce", "beer",
            "barley", "rye", "couscous", "batter", "breadcrumbs", "croutons",
            "tortilla", "pita", "cereal", "crackers", "pretzel"
    ));

    // Ingrédients non halal
    private static final Set<String> NON_HALAL_INGREDIENTS = new HashSet<>(Arrays.asList(
            "pork", "bacon", "ham", "sausage", "pepperoni", "prosciutto",
            "alcohol", "wine", "beer", "rum", "vodka", "whiskey", "brandy",
            "lard", "gelatin", "pancetta"
    ));

    // Ingrédients d'origine animale (non végétariens)
    private static final Set<String> MEAT_INGREDIENTS = new HashSet<>(Arrays.asList(
            "beef", "pork", "chicken", "lamb", "turkey", "duck", "fish",
            "salmon", "tuna", "shrimp", "prawn", "crab", "lobster", "meat",
            "bacon", "ham", "sausage", "anchovy", "seafood", "veal"
    ));

    // Ingrédients contenant du lactose
    private static final Set<String> DAIRY_INGREDIENTS = new HashSet<>(Arrays.asList(
            "milk", "cheese", "butter", "cream", "yogurt", "yoghurt",
            "parmesan", "mozzarella", "cheddar", "ricotta", "mascarpone",
            "whey", "lactose", "ghee", "buttermilk", "sour cream"
    ));

    // Cache simple
    private ArrayList<Recipe> cachedRecipes = null;
    private String lastPreferencesKey = null;

    private RequestQueue requestQueue;

    public RecipeAPIManager(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Récupère les recettes selon les préférences utilisateur avec filtrage intelligent
     */
    public void fetchRecipes(Context context, RecipeCallback callback) {
        Log.d(TAG, "fetchRecipes() appelé");

        // Lire les préférences
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        boolean isVegetarian = prefs.getBoolean("vegetarian", false);
        boolean isGlutenFree = prefs.getBoolean("gluten_free", false);
        boolean isHalal = prefs.getBoolean("halal", false);

        // Créer une clé unique pour le cache basée sur les préférences
        String preferencesKey = isVegetarian + "_" + isGlutenFree + "_" + isHalal;

        // Vérifier le cache
        if (cachedRecipes != null && preferencesKey.equals(lastPreferencesKey)) {
            Log.d(TAG, "Utilisation du cache : " + cachedRecipes.size() + " recettes");
            callback.onSuccess(cachedRecipes);
            return;
        }

        // Déterminer les catégories à récupérer
        ArrayList<String> categories = determineCategoriesToFetch(isVegetarian, isHalal);
        Log.d(TAG, "Catégories à récupérer : " + categories);

        // Récupérer les recettes de toutes les catégories
        fetchMultipleCategories(context, categories, isVegetarian, isGlutenFree, isHalal,
                preferencesKey, callback);
    }

    /**
     * Détermine les catégories à récupérer selon les préférences
     */
    private ArrayList<String> determineCategoriesToFetch(boolean isVegetarian, boolean isHalal) {
        ArrayList<String> categories = new ArrayList<>();

        if (isVegetarian) {
            // Si végétarien : seulement Vegetarian et Dessert
            categories.add("Vegetarian");
            categories.add("Dessert");
        } else if (isHalal) {
            // Si halal : éviter Pork, inclure Chicken, Seafood, Beef
            categories.add("Chicken");
            categories.add("Seafood");
            categories.add("Beef");
            categories.add("Dessert");
        } else {
            // Par défaut : mélange de plusieurs catégories
            categories.add("Chicken");
            categories.add("Seafood");
            categories.add("Vegetarian");
            categories.add("Dessert");
        }

        return categories;
    }

    /**
     * Récupère les recettes de plusieurs catégories
     */
    private void fetchMultipleCategories(Context context, ArrayList<String> categories,
                                         boolean isVegetarian, boolean isGlutenFree,
                                         boolean isHalal, String preferencesKey,
                                         RecipeCallback callback) {

        ArrayList<Recipe> allRecipes = new ArrayList<>();
        final int[] categoriesFetched = {0};

        for (String category : categories) {
            String url = BASE_URL + FILTER_BY_CATEGORY + category;
            Log.d(TAG, "Récupération catégorie : " + category);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parseAndFetchDetailsWithFilter(response, allRecipes,
                                    isVegetarian, isGlutenFree, isHalal,
                                    new RecipeCallback() {
                                        @Override
                                        public void onSuccess(ArrayList<Recipe> recipes) {
                                            categoriesFetched[0]++;

                                            // Quand toutes les catégories sont récupérées
                                            if (categoriesFetched[0] == categories.size()) {
                                                Log.d(TAG, "Total recettes après filtrage : " + allRecipes.size());

                                                if (allRecipes.isEmpty()) {
                                                    callback.onError("Aucune recette ne correspond à vos critères");
                                                } else {
                                                    // Mettre en cache
                                                    cachedRecipes = allRecipes;
                                                    lastPreferencesKey = preferencesKey;
                                                    callback.onSuccess(allRecipes);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(String errorMessage) {
                                            categoriesFetched[0]++;

                                            if (categoriesFetched[0] == categories.size()) {
                                                if (allRecipes.isEmpty()) {
                                                    callback.onError("Impossible de récupérer les recettes");
                                                } else {
                                                    cachedRecipes = allRecipes;
                                                    lastPreferencesKey = preferencesKey;
                                                    callback.onSuccess(allRecipes);
                                                }
                                            }
                                        }
                                    });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Erreur catégorie " + category + " : " + error.toString());
                            categoriesFetched[0]++;

                            if (categoriesFetched[0] == categories.size()) {
                                if (allRecipes.isEmpty()) {
                                    handleError(error, callback);
                                } else {
                                    callback.onSuccess(allRecipes);
                                }
                            }
                        }
                    }
            );

            requestQueue.add(request);
        }
    }

    /**
     * Parse la réponse et récupère les détails avec filtrage par ingrédients
     */
    private void parseAndFetchDetailsWithFilter(JSONObject response, ArrayList<Recipe> allRecipes,
                                                boolean isVegetarian, boolean isGlutenFree,
                                                boolean isHalal, RecipeCallback callback) {
        try {
            JSONArray mealsArray = response.getJSONArray("meals");
            int totalMeals = mealsArray.length();

            Log.d(TAG, "Recettes trouvées dans cette catégorie : " + totalMeals);

            if (totalMeals == 0) {
                callback.onSuccess(new ArrayList<>());
                return;
            }

            // Limiter à 15 recettes par catégorie pour éviter trop de requêtes
            int mealsToFetch = Math.min(totalMeals, 15);
            final int[] fetchedCount = {0};

            for (int i = 0; i < mealsToFetch; i++) {
                JSONObject mealObject = mealsArray.getJSONObject(i);
                String mealId = mealObject.getString("idMeal");

                fetchRecipeDetails(mealId, new RecipeCallback() {
                    @Override
                    public void onSuccess(ArrayList<Recipe> detailedRecipes) {
                        if (!detailedRecipes.isEmpty()) {
                            Recipe recipe = detailedRecipes.get(0);

                            // FILTRAGE INTELLIGENT PAR INGRÉDIENTS
                            if (isRecipeSuitable(recipe, isVegetarian, isGlutenFree, isHalal)) {
                                synchronized (allRecipes) {
                                    allRecipes.add(recipe);
                                }
                                Log.d(TAG, "Recette acceptée : " + recipe.getName());
                            } else {
                                Log.d(TAG, "Recette rejetée : " + recipe.getName());
                            }
                        }

                        fetchedCount[0]++;

                        if (fetchedCount[0] == mealsToFetch) {
                            callback.onSuccess(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        fetchedCount[0]++;

                        if (fetchedCount[0] == mealsToFetch) {
                            callback.onSuccess(new ArrayList<>());
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
     * Vérifie si une recette convient selon les préférences (filtrage par ingrédients)
     */
    private boolean isRecipeSuitable(Recipe recipe, boolean isVegetarian,
                                     boolean isGlutenFree, boolean isHalal) {

        List<String> ingredients = recipe.getIngredients();

        for (String ingredient : ingredients) {
            String ingredientLower = ingredient.toLowerCase().trim();

            // Filtrage végétarien
            if (isVegetarian && containsMeat(ingredientLower)) {
                Log.d(TAG, "Rejet (végétarien) : " + ingredient + " dans " + recipe.getName());
                return false;
            }

            // Filtrage sans gluten
            if (isGlutenFree && containsGluten(ingredientLower)) {
                Log.d(TAG, "Rejet (sans gluten) : " + ingredient + " dans " + recipe.getName());
                return false;
            }

            // Filtrage halal
            if (isHalal && isNonHalal(ingredientLower)) {
                Log.d(TAG, "Rejet (halal) : " + ingredient + " dans " + recipe.getName());
                return false;
            }
        }

        return true;
    }

    /**
     * Vérifie si un ingrédient contient de la viande
     */
    private boolean containsMeat(String ingredient) {
        for (String meat : MEAT_INGREDIENTS) {
            if (ingredient.contains(meat)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un ingrédient contient du gluten
     */
    private boolean containsGluten(String ingredient) {
        for (String gluten : GLUTEN_INGREDIENTS) {
            if (ingredient.contains(gluten)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un ingrédient est non halal
     */
    private boolean isNonHalal(String ingredient) {
        for (String nonHalal : NON_HALAL_INGREDIENTS) {
            if (ingredient.contains(nonHalal)) {
                return true;
            }
        }
        return false;
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
        lastPreferencesKey = null;
        Log.d(TAG, "Cache vidé");
    }

    public boolean hasCache() {
        return cachedRecipes != null && !cachedRecipes.isEmpty();
    }

    public ArrayList<Recipe> getCachedRecipes() {
        return cachedRecipes;
    }
}