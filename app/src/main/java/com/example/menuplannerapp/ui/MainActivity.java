package com.example.menuplannerapp.ui;
import android.util.Log;


import com.example.menuplannerapp.api.RecipeAPIManager;
import com.example.menuplannerapp.api.RecipeCallback;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.menuplannerapp.R;
import com.example.menuplannerapp.models.ShoppingItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;



//****

import android.widget.Button;

import com.example.menuplannerapp.logic.MenuGenerator;
import com.example.menuplannerapp.logic.ShoppingListGenerator;
import com.example.menuplannerapp.models.MenuItem;
import com.example.menuplannerapp.models.Recipe;




public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser les vues
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Créer et configurer l'adapter
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Connecter le TabLayout avec le ViewPager2
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

        Button btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(v -> {
            RecipeAPIManager apiManager = new RecipeAPIManager(this);

            apiManager.fetchRecipes(this, new RecipeCallback() {
                @Override
                public void onSuccess(ArrayList<Recipe> recipes) {
                    // Générer le menu hebdomadaire
                    ArrayList<MenuItem> weeklyMenu = MenuGenerator.generateWeeklyMenu(recipes);

                    // Générer la liste de courses
                    ArrayList<ShoppingItem> shoppingList = ShoppingListGenerator.generateShoppingList(weeklyMenu);

                    // Mettre à jour les fragments
                    MenuFragment menuFragment = adapter.getMenuFragment();
                    ShoppingFragment shoppingFragment = adapter.getShoppingFragment();

                    menuFragment.updateMenu(weeklyMenu);
                    shoppingFragment.updateList(shoppingList);
                }

                @Override
                public void onError(String errorMessage) {
                    // Log ou toast
                    Log.e("MainActivity", "Erreur API : " + errorMessage);
                }
            });
        });


    }
}