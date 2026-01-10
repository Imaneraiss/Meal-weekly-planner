package com.example.menuplannerapp.logic;

import com.example.menuplannerapp.models.MenuItem;
import com.example.menuplannerapp.models.ShoppingItem;
import com.example.menuplannerapp.models.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingListGenerator {

    public static ArrayList<ShoppingItem> generateShoppingList(ArrayList<MenuItem> menu) {

        HashMap<String, String> map = new HashMap<>();

        for (MenuItem item : menu) {
            Recipe r = item.getRecipe();

            if (r == null) continue;

            for (int i = 0; i < r.getIngredients().size(); i++) {
                String ingredient = r.getIngredients().get(i);
                String measure = r.getMeasures().get(i);

                if (map.containsKey(ingredient)) {
                    map.put(ingredient, map.get(ingredient) + " + " + measure);
                } else {
                    map.put(ingredient, measure);
                }
            }
        }

        ArrayList<ShoppingItem> list = new ArrayList<>();
        for (String ing : map.keySet()) {
            list.add(new ShoppingItem(ing, map.get(ing)));
        }

        return list;
    }
}
