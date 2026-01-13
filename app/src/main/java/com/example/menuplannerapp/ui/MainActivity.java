package com.example.menuplannerapp.ui;

import android.util.Log;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.menuplannerapp.R;
import com.example.menuplannerapp.api.RecipeAPIManager;
import com.example.menuplannerapp.api.RecipeCallback;
import com.example.menuplannerapp.data.database.AppDatabase;
import com.example.menuplannerapp.data.entity.MenuEntity;
import com.example.menuplannerapp.logic.MenuGenerator;
import com.example.menuplannerapp.logic.ShoppingListGenerator;
import com.example.menuplannerapp.models.MenuItem;
import com.example.menuplannerapp.models.Recipe;
import com.example.menuplannerapp.models.ShoppingItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;
    private Button btnGenerate;
    private boolean isGenerating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnGenerate = findViewById(R.id.btnGenerate);

        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Menu");
                            break;
                        case 1:
                            tab.setText("Courses");
                            break;
                    }
                }
        ).attach();

        btnGenerate.setOnClickListener(v -> {
            if (isGenerating) {
                Toast.makeText(this, "Génération en cours...", Toast.LENGTH_SHORT).show();
                return;
            }
            generateMenu();
        });
    }

    private void generateMenu() {
        isGenerating = true;
        btnGenerate.setEnabled(false);
        btnGenerate.setText("Génération...");

        RecipeAPIManager apiManager = new RecipeAPIManager(this);

        apiManager.fetchRecipes(this, new RecipeCallback() {
            @Override
            public void onSuccess(ArrayList<Recipe> recipes) {
                Log.d("MainActivity", "Recettes récupérées : " + recipes.size());

                ArrayList<MenuItem> weeklyMenu = MenuGenerator.generateWeeklyMenu(recipes);
                ArrayList<ShoppingItem> shoppingList = ShoppingListGenerator.generateShoppingList(weeklyMenu);

                // ✅ NOUVEAU : SAUVEGARDER DANS L'HISTORIQUE
                saveMenuToHistory(weeklyMenu);

                updateFragments(weeklyMenu, shoppingList);

                isGenerating = false;
                btnGenerate.setEnabled(true);
                btnGenerate.setText("Générer menu");

                Toast.makeText(MainActivity.this,
                        "Menu généré et sauvegardé !",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("MainActivity", "Erreur API : " + errorMessage);

                isGenerating = false;
                btnGenerate.setEnabled(true);
                btnGenerate.setText("Générer menu");

                Toast.makeText(MainActivity.this,
                        "Erreur : " + errorMessage,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * ✅ NOUVELLE MÉTHODE : Sauvegarde le menu dans l'historique
     */
    private void saveMenuToHistory(ArrayList<MenuItem> weeklyMenu) {
        try {
            AppDatabase db = AppDatabase.getInstance(this);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date());

            // Grouper les repas par jour
            for (int day = 0; day < 7; day++) {
                MenuEntity menuEntity = new MenuEntity();
                menuEntity.date = currentDate + " - Jour " + (day + 1);

                // Extraire les 3 repas du jour (breakfast, lunch, dinner)
                for (int meal = 0; meal < 3; meal++) {
                    int index = day * 3 + meal;
                    if (index < weeklyMenu.size()) {
                        MenuItem item = weeklyMenu.get(index);
                        String recipeName = item.getRecipe().getName();

                        switch (item.getMealType()) {
                            case "breakfast":
                                menuEntity.breakfast = recipeName;
                                break;
                            case "lunch":
                                menuEntity.lunch = recipeName;
                                break;
                            case "dinner":
                                menuEntity.dinner = recipeName;
                                break;
                        }
                    }
                }

                db.menuDao().insertMenu(menuEntity);
            }

            Log.d("MainActivity", "Menu sauvegardé dans l'historique");
        } catch (Exception e) {
            Log.e("MainActivity", "Erreur sauvegarde historique", e);
        }
    }

    private void updateFragments(ArrayList<MenuItem> weeklyMenu,
                                 ArrayList<ShoppingItem> shoppingList) {
        try {
            MenuFragment menuFragment = adapter.getMenuFragment();
            ShoppingFragment shoppingFragment = adapter.getShoppingFragment();

            if (menuFragment != null) {
                menuFragment.updateMenu(weeklyMenu);
            } else {
                Log.w("MainActivity", "MenuFragment est null");
            }

            if (shoppingFragment != null) {
                shoppingFragment.updateList(shoppingList);
            } else {
                Log.w("MainActivity", "ShoppingFragment est null");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Erreur lors de la mise à jour des fragments", e);
            Toast.makeText(this, "Erreur d'affichage", Toast.LENGTH_SHORT).show();
        }
    }
}