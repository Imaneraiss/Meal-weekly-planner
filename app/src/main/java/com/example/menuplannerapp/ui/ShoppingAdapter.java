package com.example.menuplannerapp.ui;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menuplannerapp.R;
import com.example.menuplannerapp.models.ShoppingItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShoppingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_CATEGORY = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private ArrayList<ShoppingItem> shoppingList = new ArrayList<>();
    private List<Object> displayList = new ArrayList<>();

    private static final Map<String, List<String>> CATEGORIES = new HashMap<>();
    static {
        CATEGORIES.put("Viandes & Poissons", List.of(
                "chicken", "beef", "lamb", "pork", "fish", "salmon", "tuna",
                "shrimp", "prawn", "turkey", "duck", "meat"
        ));

        CATEGORIES.put("Légumes", List.of(
                "tomato", "onion", "garlic", "carrot", "potato", "pepper",
                "cucumber", "lettuce", "spinach", "broccoli", "cabbage"
        ));

        CATEGORIES.put("Fruits", List.of(
                "apple", "banana", "orange", "lemon", "lime", "strawberry",
                "mango", "grape", "watermelon"
        ));

        CATEGORIES.put("Produits Laitiers", List.of(
                "milk", "cheese", "butter", "cream", "yogurt", "yoghurt",
                "parmesan", "mozzarella", "cheddar"
        ));

        CATEGORIES.put("Céréales & Pains", List.of(
                "rice", "pasta", "bread", "flour", "wheat", "noodles",
                "couscous", "quinoa", "oats"
        ));

        CATEGORIES.put("Épices & Condiments", List.of(
                "salt", "pepper", "sugar", "oil", "vinegar", "soy sauce",
                "cumin", "paprika", "cinnamon", "ginger", "garlic powder"
        ));

        CATEGORIES.put("Autres", List.of());
    }

    public void setShoppingList(ArrayList<ShoppingItem> list) {
        shoppingList = list;
        organizeByCategory();
        notifyDataSetChanged();
    }

    private void organizeByCategory() {
        displayList.clear();

        LinkedHashMap<String, List<ShoppingItem>> categorized = new LinkedHashMap<>();

        for (String category : CATEGORIES.keySet()) {
            categorized.put(category, new ArrayList<>());
        }

        for (ShoppingItem item : shoppingList) {
            String ingredient = item.getIngredient().toLowerCase();
            boolean found = false;

            for (Map.Entry<String, List<String>> entry : CATEGORIES.entrySet()) {
                String category = entry.getKey();

                if (category.equals("Autres")) continue;

                for (String keyword : entry.getValue()) {
                    if (ingredient.contains(keyword)) {
                        categorized.get(category).add(item);
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }

            if (!found) {
                categorized.get("Autres").add(item);
            }
        }

        for (Map.Entry<String, List<ShoppingItem>> entry : categorized.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                displayList.add(entry.getKey());
                displayList.addAll(entry.getValue());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (displayList.get(position) instanceof String)
                ? VIEW_TYPE_CATEGORY
                : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CATEGORY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_shopping_category, parent, false);
            return new CategoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_shopping, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            String category = (String) displayList.get(position);
            ((CategoryViewHolder) holder).tvCategory.setText(category);
        }
        else if (holder instanceof ItemViewHolder) {
            ShoppingItem item = (ShoppingItem) displayList.get(position);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            itemHolder.checkBox.setChecked(item.isPurchased());
            itemHolder.tvIngredient.setText(item.getIngredient());

            updateTextStyle(itemHolder, item.isPurchased());

            itemHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setPurchased(isChecked);
                updateTextStyle(itemHolder, isChecked);
            });
        }
    }

    private void updateTextStyle(ItemViewHolder holder, boolean isPurchased) {
        if (isPurchased) {
            holder.tvIngredient.setPaintFlags(
                    holder.tvIngredient.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            holder.tvIngredient.setAlpha(0.5f);
        } else {
            holder.tvIngredient.setPaintFlags(
                    holder.tvIngredient.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
            );
            holder.tvIngredient.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategoryHeader);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvIngredient;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.cbPurchased);
            tvIngredient = itemView.findViewById(R.id.tvIngredientName);
        }
    }
}