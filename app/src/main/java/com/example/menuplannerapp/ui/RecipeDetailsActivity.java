package com.example.menuplannerapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.menuplannerapp.R;
import com.example.menuplannerapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {

    private ImageView ivRecipeImage;
    private TextView tvRecipeName, tvCategory, tvArea, tvInstructions;
    private RecyclerView recyclerIngredients;
    private Button btnBackToMenu;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // Initialiser les vues
        ivRecipeImage = findViewById(R.id.ivRecipeDetailImage);
        tvRecipeName = findViewById(R.id.tvRecipeDetailName);
        tvCategory = findViewById(R.id.tvRecipeDetailCategory);
        tvArea = findViewById(R.id.tvRecipeDetailArea);
        tvInstructions = findViewById(R.id.tvRecipeInstructions);
        recyclerIngredients = findViewById(R.id.recyclerIngredients);
        btnBackToMenu = findViewById(R.id.btnBackToMenu);

        // Récupérer la recette passée en Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("recipe")) {
            recipe = (Recipe) intent.getSerializableExtra("recipe");
            displayRecipeDetails();
        }

        // Bouton retour
        btnBackToMenu.setOnClickListener(v -> finish());
    }

    private void displayRecipeDetails() {
        if (recipe == null) return;

        // Afficher l'image
        Glide.with(this)
                .load(recipe.getThumbnailUrl())
                .into(ivRecipeImage);

        // Afficher les informations de base
        tvRecipeName.setText(recipe.getName());
        tvCategory.setText("Catégorie : " + recipe.getCategory());
        tvArea.setText("Origine : " + recipe.getArea());
        tvInstructions.setText(recipe.getInstructions());

        // Afficher les ingrédients
        setupIngredientsRecyclerView();
    }

    private void setupIngredientsRecyclerView() {
        List<String> ingredients = recipe.getIngredients();
        List<String> measures = recipe.getMeasures();

        // Créer une liste combinée ingrédient + mesure
        List<String> ingredientsList = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            String measure = i < measures.size() ? measures.get(i) : "";
            if (measure.trim().isEmpty()) {
                ingredientsList.add(ingredients.get(i));
            } else {
                ingredientsList.add(measure + " " + ingredients.get(i));
            }
        }

        // Configurer le RecyclerView
        IngredientsAdapter adapter = new IngredientsAdapter(ingredientsList);
        recyclerIngredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerIngredients.setAdapter(adapter);
    }
}