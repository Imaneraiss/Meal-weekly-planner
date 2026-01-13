package com.example.menuplannerapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.menuplannerapp.R;
import com.example.menuplannerapp.models.MenuItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_MEAL = 1;

    private ArrayList<MenuItem> menuList = new ArrayList<>();
    private List<Object> displayList = new ArrayList<>();

    public void setMenuList(ArrayList<MenuItem> list) {
        menuList = list;
        organizeMenuByDay();
        notifyDataSetChanged();
    }

    /**
     * ✅ NOUVELLE MÉTHODE : Organise le menu par jour
     * Structure : [Header "Lundi", Petit-déj, Déjeuner, Dîner, Header "Mardi", ...]
     */
    private void organizeMenuByDay() {
        displayList.clear();

        // Grouper les repas par jour
        LinkedHashMap<String, List<MenuItem>> menuByDay = new LinkedHashMap<>();

        for (MenuItem item : menuList) {
            String day = item.getDay();
            if (!menuByDay.containsKey(day)) {
                menuByDay.put(day, new ArrayList<>());
            }
            menuByDay.get(day).add(item);
        }

        // Créer la liste d'affichage avec headers
        for (Map.Entry<String, List<MenuItem>> entry : menuByDay.entrySet()) {
            displayList.add(entry.getKey()); // Header (nom du jour)
            displayList.addAll(entry.getValue()); // Les 3 repas
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (displayList.get(position) instanceof String)
                ? VIEW_TYPE_HEADER
                : VIEW_TYPE_MEAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_day_header, parent, false);
            return new DayHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_menu, parent, false);
            return new MealViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DayHeaderViewHolder) {
            String day = (String) displayList.get(position);
            ((DayHeaderViewHolder) holder).tvDay.setText(day);
        } else if (holder instanceof MealViewHolder) {
            MenuItem item = (MenuItem) displayList.get(position);
            MealViewHolder mealHolder = (MealViewHolder) holder;

            mealHolder.tvMealType.setText(item.getMeal());
            mealHolder.tvName.setText(item.getRecipe().getName());
            mealHolder.tvCategory.setText(item.getRecipe().getCategory());

            Glide.with(holder.itemView.getContext())
                    .load(item.getRecipe().getThumbnailUrl())
                    .into(mealHolder.ivRecipe);
        }
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    // ✅ ViewHolder pour les headers de jour
    static class DayHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;

        DayHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tvDayHeader);
        }
    }

    // ✅ ViewHolder pour les repas
    static class MealViewHolder extends RecyclerView.ViewHolder {
        TextView tvMealType, tvName, tvCategory;
        ImageView ivRecipe;

        MealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMealType = itemView.findViewById(R.id.tvMealType);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvCategory = itemView.findViewById(R.id.tvRecipeCategory);
            ivRecipe = itemView.findViewById(R.id.ivRecipeImage);
        }
    }
}